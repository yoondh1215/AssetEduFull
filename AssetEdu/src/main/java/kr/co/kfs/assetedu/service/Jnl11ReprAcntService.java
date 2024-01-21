package kr.co.kfs.assetedu.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.repository.Jnl11ReprAcntRepository;

@Service
public class Jnl11ReprAcntService {

	@Autowired
	private Jnl11ReprAcntRepository repository;

	public List<Jnl11ReprAcnt> selectList(Condition condition) {
		return repository.selectList(condition);
	}
	
	@Transactional
	public int insert(@Valid Jnl11ReprAcnt acnt) {
		return repository.insert(acnt);
	}
	@Transactional
	public int update(Jnl11ReprAcnt acnt) {
		return repository.update(acnt);
	}

	@Transactional
	public int delete(Jnl11ReprAcnt acnt) {
		return repository.delete(acnt);
	}

	public Jnl11ReprAcnt selectOne(Jnl11ReprAcnt acnt) {
		return repository.selectOne(acnt);
	}

	public int delete(String Jnl11ReprAcntCd) {
		Jnl11ReprAcnt Jnl11ReprAcnt = new Jnl11ReprAcnt();
		Jnl11ReprAcnt.setJnl11ReprAcntCd(Jnl11ReprAcntCd);
		return delete(Jnl11ReprAcnt);
	}

	public Jnl11ReprAcnt selectOne(String Jnl11ReprAcntCd) {
		Jnl11ReprAcnt Jnl11ReprAcnt = new Jnl11ReprAcnt();
		Jnl11ReprAcnt.setJnl11ReprAcntCd(Jnl11ReprAcntCd);

		return this.selectOne(Jnl11ReprAcnt);
	}

	public long selectCount(Condition condition) {
		return repository.selectCount(condition);
	}
}
