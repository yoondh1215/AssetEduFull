package kr.co.kfs.assetedu.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.repository.Jnl10AcntRepository;

@Service
public class Jnl10AcntService {

	@Autowired
	private Jnl10AcntRepository repository;

	public int selectCount(Condition condition) {
		return repository.selectCount(condition);
	}
	
	public List<Jnl10Acnt> selectList(Condition condition) {
		return repository.selectList(condition);
	}
	
	@Transactional
	public int insert(@Valid Jnl10Acnt acnt) {
		return repository.insert(acnt);
	}
	@Transactional
	public int update(Jnl10Acnt acnt) {
		return repository.update(acnt);
	}

	@Transactional
	public int delete(Jnl10Acnt acnt) {
		return repository.delete(acnt);
	}

	public Jnl10Acnt selectOne(Jnl10Acnt acnt) {
		return repository.selectOne(acnt);
	}

	public int delete(String jnl10AcntCd) {
		Jnl10Acnt jnl10Acnt = new Jnl10Acnt();
		jnl10Acnt.setJnl10AcntCd(jnl10AcntCd);
		return delete(jnl10Acnt);
	}

	public Jnl10Acnt selectOne(String jnl10AcntCd) {
		Jnl10Acnt jnl10Acnt = new Jnl10Acnt();
		jnl10Acnt.setJnl10AcntCd(jnl10AcntCd);

		return this.selectOne(jnl10Acnt);
	}


}
