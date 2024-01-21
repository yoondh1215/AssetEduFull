package kr.co.kfs.assetedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl12Tr;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.repository.Bok01BookRepository;
import kr.co.kfs.assetedu.repository.Jnl12TrRepository;
import kr.co.kfs.assetedu.repository.Opr01ContRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Bok01BookService2  {

	@Autowired
	private Bok01BookRepository bookRepository;

	@Autowired
	private Opr01ContRepository contRepository;
	
	@Autowired
	private Jnl12TrRepository trRepository;

	@Autowired
	private Jnl01JournalService jnlService;
	
	
	@Transactional
	public String createBook(Opr01Cont cont) {

		log.debug("--------------------------------------------------------------");
		log.debug("----- createBook IN : {}", cont);

		String resultMsg = "Y";
		Long   bookAmt   = 0l;
		Long   trPl      = 0l;
		
		//원장GET
		Condition bookCondition = new Condition();
		bookCondition.put("bookId"  , cont.getOpr01BookId());
		bookCondition.put("holdDate", cont.getOpr01ContDate());
		Bok01Book book = bookRepository.selectOneByBookId(bookCondition);
		if(book == null) {
			book = new Bok01Book();
			book.setBok01BookId(cont.getOpr01BookId());
			book.setBok01HoldDate(cont.getOpr01ContDate());
			book.setBok01FundCd(cont.getOpr01FundCd());
			book.setBok01ItemCd(cont.getOpr01ItemCd());
			book.setBok01EvalYn("false");
		} else {
			if("true".equals(book.getBok01EvalYn())) {
				resultMsg = "평가가 완료되었습니다. 평가취소 후 거래하세요";
				throw new AssetException(resultMsg);
			}
		}
		
		//거래맵핑GET
		Jnl12Tr trMap = trRepository.selectOneByTrCd(cont.getOpr01TrCd());
		if(trMap.getJnl12InOutType() == null) {
			resultMsg = "원장입출고구분값이 없습니다. 분개맵핑정보를 확인하세요";
			throw new AssetException(resultMsg);
		}
		
		//---------------------------------------------
		// 입고처리
		//---------------------------------------------
		if("1".equals(trMap.getJnl12InOutType())) {
			
			bookAmt = cont.getOpr01SettleAmt();
			
			//보유수량
			Long holdQty = 0l;
			if(book.getBok01HoldQty() != null) {
				holdQty = book.getBok01HoldQty();
			}
			book.setBok01HoldQty(holdQty + cont.getOpr01Qty());
			
			//취득금액
			Long purAmt = 0l;
			if(book.getBok01PurAmt() != null) {
				purAmt = book.getBok01PurAmt();
			}
			book.setBok01PurAmt(purAmt + bookAmt);

			//장부금액
			if(book.getBok01BookAmt() != null) {
				book.setBok01BookAmt(book.getBok01BookAmt() + bookAmt);
			} else {
				book.setBok01BookAmt(bookAmt);
			}
			
			book.setBok01EvalAmt(0l);		//평가금액
			book.setBok01EvalPl(0l);		//평가손익
			book.setBok01EvalYn("false");	//평가여부
		}
		//---------------------------------------------
		// 출고처리
		//---------------------------------------------
		else if("2".equals(trMap.getJnl12InOutType())) {

			//출고가능수량 체크
			if(book.getBok01HoldQty() - cont.getOpr01Qty() < 0) {
				resultMsg = "매도수량(" + cont.getOpr01Qty() + ")이 출고가능수량(" + book.getBok01HoldQty() + ") 보다 많습니다.";
				throw new AssetException(resultMsg);
			}
			
			//전액출고SET
			if(book.getBok01HoldQty() == cont.getOpr01Qty()) {
				bookAmt = book.getBok01BookAmt();
				book.setBok01BookAmt(0l);
				book.setBok01PurAmt(0l);
			}
			//일부출고SET
			else {
				bookAmt = roundDown(book.getBok01BookAmt() / book.getBok01HoldQty() * cont.getOpr01Qty(), 1);
				book.setBok01BookAmt(book.getBok01BookAmt() - bookAmt);
				
				Long purAmt = roundDown(book.getBok01PurAmt() / book.getBok01HoldQty() * cont.getOpr01Qty(), 1);
				book.setBok01PurAmt(book.getBok01PurAmt() - purAmt);
			}

			//보유수량
			book.setBok01HoldQty(book.getBok01HoldQty() - cont.getOpr01Qty());
			
			//처분손익
			trPl = cont.getOpr01ContAmt() - bookAmt;
		}
		//---------------------------------------------
		// 평가처리
		//---------------------------------------------
		else if("3".equals(trMap.getJnl12InOutType())) {
			book.setBok01EvalAmt(cont.getOpr01ContAmt());
			book.setBok01EvalPl(cont.getOpr01TrPl());
			book.setBok01EvalYn("true");
			bookAmt = cont.getOpr01BookAmt();
			trPl = cont.getOpr01TrPl();
		} else {
			resultMsg = "미정의 처리코드입니다. 관리팀에 문의하세요 (" + trMap.getJnl12InOutType() + ")";
			throw new AssetException(resultMsg);
		}
		
		//보유원장 Insert & Update
		int dmlCnt = bookRepository.upsert(book);
		log.debug("보유원장 Insert & Update Count : {}", dmlCnt);
		
		//거래내역 장부금액 UPDATE
		cont.setOpr01BookAmt(bookAmt);
		cont.setOpr01TrPl(trPl);
		int updateCnt = contRepository.update(cont);
		log.debug("거래내역 장부금액 UPDATE Count : {}", updateCnt);

		//분개생성
		resultMsg = jnlService.createJournal(cont);
		
		return resultMsg;
	}
	
	private long roundDown(double number, double place) { 
		double result = number / place; 
		result = Math.floor(result); 
		result *= place; 
		return (long)result; 
	}
}
