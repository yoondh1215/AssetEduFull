package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl10Acnt;

public interface Jnl10AcntRepository {
	// list and totalcount
	List<Jnl10Acnt> selectList(Condition condition);
	Integer selectCount(Condition condition);
	
	// CRUD
	Jnl10Acnt selectOne(Jnl10Acnt acnt);
	int insert(Jnl10Acnt acnt);
	int update(Jnl10Acnt acnt);
	int delete(Jnl10Acnt acnt);
}
