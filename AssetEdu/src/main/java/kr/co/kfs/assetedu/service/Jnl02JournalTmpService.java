package kr.co.kfs.assetedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Jnl02JournalTmp;
import kr.co.kfs.assetedu.repository.Jnl02JournalTmpRepository;

@Service
public class Jnl02JournalTmpService  {

	@Autowired
	private Jnl02JournalTmpRepository journalTmpRepository;
	
	@Transactional
	public int insert(Jnl02JournalTmp fund) {
		return journalTmpRepository.insert(fund);
	}
	@Transactional
	public int update(Jnl02JournalTmp fund) {
		return journalTmpRepository.update(fund);
	}
	@Transactional
	public int delete(Jnl02JournalTmp  fund ) {
		return journalTmpRepository.delete(fund);
	}

}
