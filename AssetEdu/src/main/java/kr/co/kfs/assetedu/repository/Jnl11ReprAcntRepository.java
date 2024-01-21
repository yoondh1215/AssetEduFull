package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;

public interface Jnl11ReprAcntRepository {
	// list and totalcount
	List<Jnl11ReprAcnt> selectList(Condition condition);
	Integer selectCount(Condition condition);
	
	// CRUD
	Jnl11ReprAcnt selectOne(Jnl11ReprAcnt acnt);
	String selectTgtReprAcntCode(String reprAcntCode);
	int insert(Jnl11ReprAcnt acnt);
	int update(Jnl11ReprAcnt acnt);
	int delete(Jnl11ReprAcnt acnt);
}
