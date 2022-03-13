package com.flysafe.FlySafe.Database.DataStore;

import static org.junit.Assert.assertEquals;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PaymentInfoRepositoryTest {
	
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private PaymentInfoRepository cardRepo;
	
	@Before
	public void setUp() throws Exception {
    	entityManagerFactory = Persistence.createEntityManagerFactory("FlySafeDB");
    	entityManager = entityManagerFactory.createEntityManager();
		cardRepo = new PaymentInfoRepository(entityManager);
	}
	
	@After
	public void tearDown() throws Exception {
		cardRepo = null;
		entityManager = null;
		entityManagerFactory = null;
	}
	@Test
	public void testFindById() {
		Optional<PaymentInfo> saved = cardRepo.save(DBPrototype.getPaymentInfo());
		assertEquals(true, saved.isPresent());
		
		Optional<PaymentInfo> found = cardRepo.findById(saved.get().getPaymentID());
		assertEquals(true, found.isPresent());
		
		Optional<PaymentInfo> notFound = cardRepo.findById(15);
		assertEquals(false, notFound.isPresent());
	}
	
	@Test
	public void testFindByCardNum() {
		Optional<PaymentInfo> saved = cardRepo.save(DBPrototype.getPaymentInfo());
		assertEquals(true, saved.isPresent());
		
		Optional<PaymentInfo> found = cardRepo.findByCardNum(saved.get().getCardNum());
		assertEquals(true, found.isPresent());
		
		Optional<PaymentInfo> notFound = cardRepo.findByCardNum("9999888877776666");
		assertEquals(false, notFound.isPresent());
	}
}
