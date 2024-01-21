package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Itm02EvalPrice;
import kr.co.kfs.assetedu.repository.Itm02EvalPriceRepository;

@Service
public class Itm02EvalPriceService  {

	@Autowired
	private Itm02EvalPriceRepository priceRepository;
	
	public List<Itm02EvalPrice> selectList(Condition condition){
		return priceRepository.selectList(condition);
	}

	@Transactional
	public int update(Itm02EvalPrice price) {
		return priceRepository.upsert(price);
	}
}
