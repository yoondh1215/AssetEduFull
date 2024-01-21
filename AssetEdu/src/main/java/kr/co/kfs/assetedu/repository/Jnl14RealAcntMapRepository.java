package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl14RealAcntMap;

public interface Jnl14RealAcntMapRepository {
	// list and totalcount
	List<Jnl14RealAcntMap> selectList(Condition condition);
	Integer selectCount(Condition condition);
	
	// CRUD
	Jnl14RealAcntMap selectOne(Jnl14RealAcntMap acnt);
	String selectRealAcntCode(Condition condition);
	int insert(Jnl14RealAcntMap acnt);
	int update(Jnl14RealAcntMap acnt);
	int delete(Jnl14RealAcntMap acnt);
}
