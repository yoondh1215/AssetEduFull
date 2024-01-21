package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl12Tr;

public interface Jnl12TrRepository {
	// list and totalcount
	List<Jnl12Tr> selectList(Condition condition);
	Integer selectCount(Condition condition);
	
	// CRUD
	Jnl12Tr selectOne(Jnl12Tr acnt);
	Jnl12Tr selectOneByTrCd(String trCode);

	int insert(Jnl12Tr acnt);
	int update(Jnl12Tr acnt);
	int delete(Jnl12Tr acnt);
}
