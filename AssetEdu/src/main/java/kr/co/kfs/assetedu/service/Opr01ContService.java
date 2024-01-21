package kr.co.kfs.assetedu.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Com03Date;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.repository.Bok01BookRepository;
import kr.co.kfs.assetedu.repository.Com03DateRepository;
import kr.co.kfs.assetedu.repository.Opr01ContRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Opr01ContService  {

	@Autowired
	private Opr01ContRepository contRepository;

	@Autowired
	private Bok01BookRepository bookRepository;

	@Autowired
	private Com03DateRepository dateRepository;

	@Autowired
	private Bok01BookService2 bookService2;

	
	public Opr01Cont selectOne(Opr01Cont cont) {
		return contRepository.selectOne(cont);
	}
	
	public List<Opr01Cont> selectList(Condition condition){
		return contRepository.selectList(condition);
	}
	
	public Long selectCount(Condition condition){
		return contRepository.selectCount(condition);
	}

	public String newContId(){
		return contRepository.newContId();
	}

	@Transactional
	public String insert(Opr01Cont cont) throws Exception {

		//체결ID
		cont.setOpr01ContId(contRepository.newContId());

		//체결상태 SET
		cont.setOpr01StatusCd("0"); //(0:미처리)

		//매수거래인 경우 보유ID SET
		String trCd = cont.getOpr01TrCd();
		if("1001".equals(trCd) || "1002".equals(trCd)) {
			String bookId;
			Condition condition = new Condition();
			condition.put("holdDate", cont.getOpr01ContDate());
			condition.put("fundCode", cont.getOpr01FundCd());
			condition.put("itemCode", cont.getOpr01ItemCd());
			
			Bok01Book book = bookRepository.selectByItemCode(condition);
			if(book == null) {
				bookId = bookRepository.newBookId();
			} else {
				if("true".equals(book.getBok01EvalYn())) {
					throw new AssetException("평가처리된 종목입니다. 평가취소 후 처리하세요");
				}
				bookId = book.getBok01BookId();
			}
			cont.setOpr01BookId(bookId);
		}

		// 거래 insert
		int procCnt = contRepository.insert(cont);
		log.debug("inserted count : {}", procCnt);

		// 보유원장&분개장 생성
		String resultMsg = this.procMain("P", cont);

		return resultMsg;
	}

	@Transactional
	public int update(Opr01Cont cont) {
		return contRepository.update(cont);
	}

	@Transactional
	public String delete(Opr01Cont cont ) throws Exception {

		//취소처리전 평가여부 확인
		Condition bookCondition = new Condition();
		bookCondition.put("bookId"  , cont.getOpr01BookId());
		bookCondition.put("holdDate", cont.getOpr01ContDate());

		Bok01Book book = bookRepository.selectOneByBookId(bookCondition);
		
		if("true".equals(book.getBok01EvalYn())) {
			throw new AssetException("평가처리된 종목입니다. 평가취소 후 가능합니다.");
		}

		// 보유원장&분개장 취소처리
		String resultMsg = this.procMain("C", cont);

		return resultMsg;
	}

	@Transactional
	public String procMain(String procType, Opr01Cont cont) throws Exception {
		
		log.debug("--------------------------------------------------------------");
		log.debug("----- procMain IN : {} - {}", procType, cont);

		String resultMsg = "Y";

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		
		//-----------------------------------------------------
		// 처리 (P:처리/A:원장이월)
		//-----------------------------------------------------
		if("P".equals(procType) || "A".equals(procType)) {

			//최근보유일GET
			String lastDate = bookRepository.selectOneLastDate();

			//'최종보유일 < 처리일'인 경우 원장이월
			if(lastDate != null) {
				if(transFormat.parse(lastDate).compareTo(transFormat.parse(cont.getOpr01ContDate())) < 0) {
					log.debug("----- '최종보유일({}) < 처리일({})'인 경우임!!!", lastDate, cont.getOpr01ContDate());

					//최종보유일+1일 ~ 처리일 원장생성
					Condition dateCondition      = new Condition();
					Condition startDateCondition = new Condition();
					
					startDateCondition.put("date"  , lastDate);
					startDateCondition.put("period", 1);
					dateCondition.put("startDate", dateRepository.getDate(startDateCondition));

					dateCondition.put("closeDate", cont.getOpr01ContDate());

					log.debug("----- 원장이월 기간 : {}", dateCondition);
					
					List<Com03Date> dateList = dateRepository.selectListByPeriod(dateCondition);
					for(Com03Date dateModel : dateList) {
						
						Condition bookCondition = new Condition();
						bookCondition.put("holdDate", dateModel.getCom03Day());

						int insertCnt = bookRepository.insertByDayBefore(bookCondition);
						log.debug("----- 원장이월 완료 : insertCnt({})", insertCnt);
					}
				}
			}
			
			if("P".equals(procType)) {
				
				//원장반영 및 분개생성
				resultMsg = bookService2.createBook(cont);
				log.debug("----- 원장반영 및 분개생성 성공");
				
				//처리상태변경
				cont.setOpr01StatusCd("1"); //(1:처리완료)
				int updateCnt = contRepository.update(cont);
				log.debug("----- 처리상태변경 완료 : updateCnt({})", updateCnt);
			}
		}
		//-----------------------------------------------------
		// 취소
		//-----------------------------------------------------
		else if("C".equals(procType)) {

			//처리상태변경
			cont.setOpr01StatusCd("9"); //(9:취소)
			int updateCnt = contRepository.update(cont);
			log.debug("----- 처리상태변경(처리완료=>취소) : updateCnt({})", updateCnt);
			
			Condition bookCondition = new Condition();
			bookCondition.put("bookId"  , cont.getOpr01BookId());
			bookCondition.put("holdDate", cont.getOpr01ContDate());

			//당일원장삭제
			int deleteCnt = bookRepository.deleteByHoldDate(bookCondition);
			log.debug("----- 당일원장삭제 완료 : deleteCnt({})", deleteCnt);

			//전일원장이월
			int insertCnt = bookRepository.insertByDayBefore(bookCondition);
			log.debug("----- 전일원장이월 완료 : insertCnt({})", insertCnt);
			
			//동일원장의 당일 다른거래 재처리
			Condition otherContCondition = new Condition();
			otherContCondition.put("contDate", cont.getOpr01ContDate());
			otherContCondition.put("bookId", cont.getOpr01BookId());
			
			List<Opr01Cont> otherContList = contRepository.selectByBookId(otherContCondition);
			for(Opr01Cont otherCont : otherContList) {
				//원장반영 및 분개생성
				resultMsg = bookService2.createBook(otherCont);
			}
		}

		return resultMsg;
	}

}
