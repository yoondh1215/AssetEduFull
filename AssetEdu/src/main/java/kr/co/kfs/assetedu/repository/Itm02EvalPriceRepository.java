package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Itm02EvalPrice;

public interface Itm02EvalPriceRepository {

	List<Itm02EvalPrice> selectList(Condition condition);
	
	// CRUD
	int upsert(Itm02EvalPrice price);
}
