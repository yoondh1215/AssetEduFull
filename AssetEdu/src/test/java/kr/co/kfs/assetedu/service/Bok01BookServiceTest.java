package kr.co.kfs.assetedu.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.kfs.assetedu.model.Bok01Book;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
class Bok01BookServiceTest {
	@Autowired
	private Bok01BookService service;
	
	@Test
	void test() {
		List<Bok01Book> list = service.selectList(null);
		for (Bok01Book bok01Book : list) {
			log.debug(bok01Book.toString());
		}
		assertNotNull(list);
	}

}
