package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Condition;

public interface Bok01BookRepository {
	// list and totalcount
	List<Bok01Book> selectList(Condition condition);
	Long selectCount(Condition condition);
	
	// CRUD
	List<Bok01Book> selectEvalList(Condition condition);
	Bok01Book selectOne(Bok01Book book);
	Bok01Book selectOneByBookId(Condition condition);
	Bok01Book selectByItemCode(Condition condition);

	String newBookId();
	String updateEval(Condition condition);
	String selectOneLastDate();

	int insert(Bok01Book book);
	int insertByDayBefore(Condition condition);
	
	int update(Bok01Book book);
	int upsert(Bok01Book book);
	
	int delete(Bok01Book book);
	int deleteByHoldDate(Condition condition);
}
