package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Opr01Cont;

public interface Opr01ContRepository {
	// list and totalcount
	List<Opr01Cont> selectList(Condition condition);
	Long selectCount(Condition condition);
	
	String newContId();
	Opr01Cont selectOne(Opr01Cont cont);
	List<Opr01Cont> selectByBookId(Condition condition);

	int insert(Opr01Cont cont);
	int update(Opr01Cont cont);
	int delete(Opr01Cont cont);
}
