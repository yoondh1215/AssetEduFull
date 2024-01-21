package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Jnl01Journal;
import kr.co.kfs.assetedu.model.Jnl02JournalTmp;
import kr.co.kfs.assetedu.model.Jnl13TrMap;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.repository.Itm01ItemRepository;
import kr.co.kfs.assetedu.repository.Jnl01JournalRepository;
import kr.co.kfs.assetedu.repository.Jnl02JournalTmpRepository;
import kr.co.kfs.assetedu.repository.Jnl11ReprAcntRepository;
import kr.co.kfs.assetedu.repository.Jnl13TrMapRepository;
import kr.co.kfs.assetedu.repository.Jnl14RealAcntMapRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Jnl01JournalService  {

	@Autowired
	private Jnl01JournalRepository journalRepository;

	@Autowired
	private Jnl02JournalTmpRepository journalTmpRepository;

	@Autowired
	private Jnl11ReprAcntRepository reprAcntRepository;

	@Autowired
	private Jnl13TrMapRepository trMapRepository;

	@Autowired
	private Jnl14RealAcntMapRepository realAcntRepository;

	@Autowired
	private Itm01ItemRepository itemRepository;

	
	public Jnl01Journal selectOne(Jnl01Journal fund) {
		return journalRepository.selectOne(fund);
	}
	
	public List<Jnl01Journal> selectList(Condition condition){
		return journalRepository.selectList(condition);
	}
	
	public Long selectCount(Condition condition){
		return journalRepository.selectCount(condition);
	}

	@Transactional
	public String createJournal(Opr01Cont cont) {

		log.debug("--------------------------------------------------------------");
		log.debug("----- createJournal IN : {}", cont);

		String resultMsg = "Y";
		int dmlCnt  = 0;
		int lastSeq = 0;

		//----------------------------------------------
		// 기존 분개내역 정리(분개장, 임시분개장)
		dmlCnt = journalTmpRepository.deleteByContId(cont.getOpr01ContId());
		log.debug("----- 임시분개장 정리 : {}", dmlCnt);

		dmlCnt = journalRepository.deleteByContId(cont.getOpr01ContId());
		log.debug("----- 분개장 정리 : {}", dmlCnt);

		//----------------------------------------------
		// 분개맵 가져오기 => 임시분개장 생성
		Jnl02JournalTmp jnlTmp;
		List<Jnl13TrMap> trMapList = trMapRepository.selectByTrCode(cont.getOpr01TrCd());
		for(Jnl13TrMap trMap : trMapList) {

			jnlTmp = new Jnl02JournalTmp();
			jnlTmp.setJnl02ContId(cont.getOpr01ContId());
			jnlTmp.setJnl02Seq(trMap.getJnl13Seq());
			jnlTmp.setJnl02DrcrType(trMap.getJnl13DrcrType());

			//대표계정코드SET
			jnlTmp.setJnl02ReprAcntCd(trMap.getJnl13ReprAcntCd());

			//대표계정코드가 '미수(1200)/미지급금(2200)'인 경우 결제일자 체크하여 당일이면 '예금(8888)'으로 변경
			if("1200".equals(trMap.getJnl13ReprAcntCd()) || "2200".equals(trMap.getJnl13ReprAcntCd())) {
				if(cont.getOpr01SettleDate().equals(cont.getOpr01ContDate())) {
					jnlTmp.setJnl02ReprAcntCd("8888");
				}
			}

			//금액SET
			String sql =  "SELECT " + trMap.getJnl13Formula();
			       sql += "  FROM opr01_cont";
			       sql += " WHERE opr01_cont_id = ";
			log.debug("----- sql : {}", sql);

			Condition sqlCondition = new Condition();
			sqlCondition.put("sql", sql);
			sqlCondition.put("contId", cont.getOpr01ContId());
			
			Long amt = trMapRepository.getAmt(sqlCondition);
			log.debug("----- amt : {}", amt);
			jnlTmp.setJnl02Amt(amt);
			
			if(amt != 0) {
				//임시분개장 Insert (금액이 0이면 제외)
				dmlCnt = journalTmpRepository.insert(jnlTmp);
			}
			lastSeq = jnlTmp.getJnl02Seq();
		}

		//----------------------------------------------
		// 처분손익 SET
		Long pl = journalTmpRepository.selectDiffAmt(cont.getOpr01ContId());
		if(pl != null && pl != 0) {
			//(2001:장내매도/2002:장외매도)이 아닌경우 차대금액은 동일해야함
			if(!"2001".equals(cont.getOpr01TrCd()) && !"2002".equals(cont.getOpr01TrCd())) {
				resultMsg = "차/대 금액이 다릅니다";
				throw new AssetException(resultMsg);
			}

			jnlTmp = new Jnl02JournalTmp();
			jnlTmp.setJnl02ContId(cont.getOpr01ContId());
			jnlTmp.setJnl02Seq(lastSeq + 1);
			jnlTmp.setJnl02ReprAcntCd("4100");	//(4100:처분이익)
			jnlTmp.setJnl02DrcrType("C");
			jnlTmp.setJnl02Amt(pl);

			//임시분개장 Insert
			dmlCnt = journalTmpRepository.insert(jnlTmp);
		}

		//----------------------------------------------
		// 금액이 '-'인 경우 반대계정으로 변경
		List<Jnl02JournalTmp> jnlTmpList1 = journalTmpRepository.selectByContId(cont.getOpr01ContId());
		for(Jnl02JournalTmp jnlTmpModel : jnlTmpList1) {
			if(jnlTmpModel.getJnl02Amt() < 0) {
				
				//대표계정코드
				jnlTmpModel.setJnl02ReprAcntCd(reprAcntRepository.selectTgtReprAcntCode(jnlTmpModel.getJnl02ReprAcntCd()));

				//금액
				jnlTmpModel.setJnl02Amt(jnlTmpModel.getJnl02Amt() * -1);
				
				//차대구분
				if("D".equals(jnlTmpModel.getJnl02DrcrType())) {
					jnlTmpModel.setJnl02DrcrType("C");
				} else {
					jnlTmpModel.setJnl02DrcrType("D");
				}
				
				//임시분개장 UPDATE
				dmlCnt = journalTmpRepository.update(jnlTmpModel);
			}
		}

		//종목코드GET
		Itm01Item item = itemRepository.selectByItemCode(cont.getOpr01ItemCd());
		
		if(item.getItm01ListType() == null || "".equals(item.getItm01ListType())) {
			throw new AssetException("상장구분이 없습니다. 종목정보를 확인하세요");
		}
		
		if(item.getItm01MarketType() == null || "".equals(item.getItm01MarketType())) {
			throw new AssetException("시장구분이 없습니다. 종목정보를 확인하세요");
		}

		Condition realAcntCondition = new Condition();
		realAcntCondition.put("listType", item.getItm01ListType());
		realAcntCondition.put("marketType", item.getItm01MarketType());

		//----------------------------------------------
		// 임시분개장 가져오기 => 분개장 생성
		int    drSeq = 0;
		int    crSeq = 0;
		String dmlType;
		Jnl01Journal insertJnl;

		List<Jnl02JournalTmp> jnlTmpList2 = journalTmpRepository.selectByContId(cont.getOpr01ContId());
		for(Jnl02JournalTmp jnlTmpModel : jnlTmpList2) {

			insertJnl = new Jnl01Journal();
			insertJnl.setJnl01ContId(cont.getOpr01ContId());

			//실계정코드 SET
			realAcntCondition.put("reprAcntCode", jnlTmpModel.getJnl02ReprAcntCd());
			String acntCode = realAcntRepository.selectRealAcntCode(realAcntCondition);
			if(acntCode == null) {
				resultMsg = "실계정코드를 가져올 수 없습니다. 관리팀에 문의하세요";
				throw new AssetException(resultMsg);
			}
			
			if("D".equals(jnlTmpModel.getJnl02DrcrType())) {
				drSeq++;
				dmlType = "I";
				
				insertJnl.setJnl01Seq(drSeq);
				insertJnl.setJnl01DrAcntCd(acntCode);
				insertJnl.setJnl01DrAmt(jnlTmpModel.getJnl02Amt());
			}
			else {
				crSeq++;
				insertJnl.setJnl01Seq(crSeq);

				if(drSeq >= crSeq) {
					dmlType = "U";
					insertJnl = journalRepository.selectOne(insertJnl);
				} else {
					dmlType = "I";
				}
				insertJnl.setJnl01CrAcntCd(acntCode);;
				insertJnl.setJnl01CrAmt(jnlTmpModel.getJnl02Amt());
			}
			
			if("I".equals(dmlType)) {
				dmlCnt = journalRepository.insert(insertJnl);
			} else {
				dmlCnt = journalRepository.update(insertJnl);
			}
		}
		
		return resultMsg;
	}
	
	@Transactional
	public int insert(Jnl01Journal fund) {
		return journalRepository.insert(fund);
	}
	@Transactional
	public int update(Jnl01Journal fund) {
		return journalRepository.update(fund);
	}
	@Transactional
	public int delete(Jnl01Journal  fund ) {
		return journalRepository.delete(fund);
	}
}
