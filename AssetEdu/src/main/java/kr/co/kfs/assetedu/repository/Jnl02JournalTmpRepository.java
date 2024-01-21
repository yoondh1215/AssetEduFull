package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl02JournalTmp;

/**
 * 임시분개장 Repository 
 *
 */
public interface Jnl02JournalTmpRepository {

	List<Jnl02JournalTmp> selectByContId(String contId);
	Long selectDiffAmt(String contId);

	int insert(Jnl02JournalTmp journal);
	int update(Jnl02JournalTmp journal);
	int delete(Jnl02JournalTmp journal);
	int deleteByContId(String contId);
}
