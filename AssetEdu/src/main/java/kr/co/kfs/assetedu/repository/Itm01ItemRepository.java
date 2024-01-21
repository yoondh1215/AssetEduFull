package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Itm01Item;

public interface Itm01ItemRepository {
	// list and totalcount
	List<Itm01Item> selectList(Condition condition);
	Long selectCount(Condition condition);
	
	// CRUD
	Itm01Item selectOne(Itm01Item item);
	Itm01Item selectByItemCode(String itemCode);
	int insert(Itm01Item item);
	int update(Itm01Item item);
	int delete(Itm01Item item);
}
