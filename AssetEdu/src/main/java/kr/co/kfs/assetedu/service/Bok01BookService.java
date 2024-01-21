package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.repository.Bok01BookRepository;
import kr.co.kfs.assetedu.repository.Opr01ContRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;

@Service
public class Bok01BookService  {

	@Autowired
	private Bok01BookRepository bookRepository;

	@Autowired
	private Opr01ContRepository contRepository;
	
	@Autowired
	private Opr01ContService contService;

	
	public Bok01Book selectOne(Bok01Book book) {
		return bookRepository.selectOne(book);
	}
	
	public List<Bok01Book> selectList(Condition condition){
		return bookRepository.selectList(condition);
	}

	public List<Bok01Book> selectEvalList(Condition condition){
		return bookRepository.selectEvalList(condition);
	}
	
	public Long selectCount(Condition condition){
		return bookRepository.selectCount(condition);
	}
	
	@Transactional
	public String updateEval(String procType, String evalDate) throws Exception {
		
		int    resultCnt = 0;
		String resultMsg = "Y";
		String trCode    = "3001";	//거래코드(3001:평가처리)
		
		Condition evalListCondition = new Condition();
		evalListCondition.put("evalDate", evalDate);
		List<Bok01Book> list = bookRepository.selectEvalList(evalListCondition);
		for(Bok01Book book : list) {

			//--------------------------------------------------------
			// 평가처리
			if("P".equals(procType) && "false".equals(book.getBok01EvalYn())) {
				Opr01Cont cont = new Opr01Cont();

				//평가내역 SET
				cont.setOpr01ContId(contRepository.newContId());	    //체결ID
				cont.setOpr01FundCd(book.getBok01FundCd());			    //펀드코드
				cont.setOpr01ItemCd(book.getBok01ItemCd()); 		    //종목코드
				cont.setOpr01ContDate(book.getBok01HoldDate());		    //체결일자
				cont.setOpr01TrCd(trCode);    						    //거래코드(3001:평가처리)
				cont.setOpr01Qty(book.getBok01HoldQty()); 			    //수량(보유주수)
				cont.setOpr01Price(book.getBok01EvalPrice()); 		    //단가(평가단가)
				
				Long evalAmt = book.getBok01HoldQty() * book.getBok01EvalPrice();
				cont.setOpr01ContAmt(evalAmt);							//체결금액(평가금액)

				cont.setOpr01TrPl(evalAmt - book.getBok01BookAmt());	//거래손익(평가손익)
				cont.setOpr01BookId(book.getBok01BookId());				//원장ID
				cont.setOpr01BookAmt(book.getBok01BookAmt()); 			//장부금액
				cont.setOpr01StatusCd("0");								//상태코드(0:미처리)

				// Insert
				int procCnt = contRepository.insert(cont);
				resultCnt++;
				
				//원장반영 & 분개내역 생성
				resultMsg = contService.procMain("P", cont);
			}
			//--------------------------------------------------------
			// 평가취소
			else if("C".equals(procType) && "true".equals(book.getBok01EvalYn())) {
				
				//평가내역 GET
				Opr01Cont cont = new Opr01Cont();
				cont.setOpr01ContId(book.getBok01ContId());
				cont = contRepository.selectOne(cont);
				resultCnt++;

				//원장반영 & 분개내역 생성
				resultMsg = contService.procMain("C", cont);
			}
		}
		
		if(resultCnt == 0) {
			if("P".equals(procType)) {
				resultMsg = "평가처리 대상이 없습니다";
			} else {
				resultMsg = "평가취소 대상이 없습니다";
			}
			throw new AssetException(resultMsg);
		}

		return resultMsg;
	}
	
	@Transactional
	public int insert(Bok01Book book) {
		return bookRepository.insert(book);
	}
	
	@Transactional
	public int update(Bok01Book book) {
		return bookRepository.update(book);
	}
	
	@Transactional
	public int delete(Bok01Book  book ) {
		return bookRepository.delete(book);
	}
}
