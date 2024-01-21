package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Fnd01Fund;

public interface Fnd01FundRepository {
	// list and totalcount
	List<Fnd01Fund> selectList(Condition condition);
	Long selectCount(Condition condition);
	
	// CRUD
	Fnd01Fund selectOne(Fnd01Fund fund);
	int insert(Fnd01Fund fund);
	int update(Fnd01Fund fund);
	int delete(Fnd01Fund fund);
}
