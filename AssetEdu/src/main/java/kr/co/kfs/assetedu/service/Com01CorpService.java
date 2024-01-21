package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.repository.Com01CorpRepository;

@Service
public class Com01CorpService {
	
	@Autowired
	private  Com01CorpRepository corpRespository;
	
	public Com01Corp selectOne(Com01Corp com01Corp){
		return corpRespository.selectOne(com01Corp);
	}

	public List<Com01Corp> selectList(Condition condition){
		return corpRespository.selectList(condition);
	}
	
	public Long selectCount(Condition condition){
		return corpRespository.selectCount(condition);
	}
	
	@Transactional
	public int insert(Com01Corp corp) {
		return corpRespository.insert(corp);
	}
	@Transactional
	public int update(Com01Corp corp) {
		return corpRespository.update(corp);
	}
	@Transactional
	public int delete(Com01Corp  corp ) {
		return corpRespository.delete(corp);
	}
}
