package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl01Journal;

/**
 * 분개장 Repository 
 *
 */
public interface Jnl01JournalRepository {

	List<Jnl01Journal> selectList(Condition condition);
	Long selectCount(Condition condition);

	// CRUD
	Jnl01Journal selectOne(Jnl01Journal journal);
	int insert(Jnl01Journal journal);
	int update(Jnl01Journal journal);
	int delete(Jnl01Journal journal);
	int deleteByContId(String contId);
}
