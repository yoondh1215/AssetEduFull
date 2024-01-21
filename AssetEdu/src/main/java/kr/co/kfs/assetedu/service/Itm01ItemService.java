package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.repository.Itm01ItemRepository;

@Service
public class Itm01ItemService  {

	@Autowired
	private Itm01ItemRepository itemRepository;

	
	public Itm01Item selectOne(Itm01Item item) {
		return itemRepository.selectOne(item);
	}
	
	public List<Itm01Item> selectList(Condition condition){
		return itemRepository.selectList(condition);
	}
	
	public Long selectCount(Condition condition){
		return itemRepository.selectCount(condition);
	}
	
	@Transactional
	public int insert(Itm01Item item) {
		return itemRepository.insert(item);
	}
	@Transactional
	public int update(Itm01Item item) {
		return itemRepository.update(item);
	}
	@Transactional
	public int delete(Itm01Item  item ) {
		return itemRepository.delete(item);
	}
}
