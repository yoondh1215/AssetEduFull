package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Fnd01Fund;
import kr.co.kfs.assetedu.repository.Fnd01FundRepository;

@Service
public class Fnd01FundService  {

	@Autowired
	private Fnd01FundRepository fundRepository;
	
	public Fnd01Fund selectOne(Fnd01Fund fund) {
		return fundRepository.selectOne(fund);
	}
	
	public List<Fnd01Fund> selectList(Condition condition){
		return fundRepository.selectList(condition);
	}
	
	public Long selectCount(Condition condition){
		return fundRepository.selectCount(condition);
	}
	
	@Transactional
	public int insert(Fnd01Fund fund) {
		return fundRepository.insert(fund);
	}
	@Transactional
	public int update(Fnd01Fund fund) {
		return fundRepository.update(fund);
	}
	@Transactional
	public int delete(Fnd01Fund  fund ) {
		return fundRepository.delete(fund);
	}
}
