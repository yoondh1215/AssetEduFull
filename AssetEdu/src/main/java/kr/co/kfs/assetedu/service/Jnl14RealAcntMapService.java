package kr.co.kfs.assetedu.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl14RealAcntMap;
import kr.co.kfs.assetedu.repository.Jnl14RealAcntMapRepository;

@Service
public class Jnl14RealAcntMapService {

	@Autowired
	private Jnl14RealAcntMapRepository repository;

	public List<Jnl14RealAcntMap> selectList(Condition condition) {
		return repository.selectList(condition);
	}
	
	@Transactional
	public int insert(@Valid Jnl14RealAcntMap acnt) {
		return repository.insert(acnt);
	}
	@Transactional
	public int update(Jnl14RealAcntMap acnt) {
		return repository.update(acnt);
	}

	@Transactional
	public int delete(Jnl14RealAcntMap acnt) {
		return repository.delete(acnt);
	}

	public Jnl14RealAcntMap selectOne(Jnl14RealAcntMap acnt) {
		return repository.selectOne(acnt);
	}

	public int delete(String jnl14ReprAcntCd) {
		Jnl14RealAcntMap ram = new Jnl14RealAcntMap();
		ram.setJnl14RealAcntCd(jnl14ReprAcntCd);
		return this.delete(ram);
	}

	public Jnl14RealAcntMap selectOne(String jnl14ReprAcntCd) {
		Jnl14RealAcntMap ram = new Jnl14RealAcntMap();
		ram.setJnl14RealAcntCd(jnl14ReprAcntCd);
		return this.selectOne(ram);
	}


}
