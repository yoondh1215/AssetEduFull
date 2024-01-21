package kr.co.kfs.assetedu.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Jnl01Journal;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
class Jnl01JournalServiceTest {

	@Autowired
	private Jnl01JournalService service;

	@BeforeEach
	@Transactional
	public void setUp() {
		Jnl01Journal journal = new Jnl01Journal();
		
		assertNotNull(journal);
		assertNotNull(service);
	}
	
	@Test
	void testSelectOne() {
		
		List<Jnl01Journal> list = service.selectList(null);
		log.debug("list.size: {}",  list.size());
		assertNotNull(list);
		assertTrue(list.size() > 0);
	}


}
