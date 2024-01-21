package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.Condition;

public interface Com01CorpRepository {
	
	Com01Corp selectOne(Com01Corp corp);
	List<Com01Corp> selectList(Condition condition);
	Long selectCount(Condition condition);
	int insert(Com01Corp corp);
	int update(Com01Corp corp);
	int delete(Com01Corp corp);
	
} 
