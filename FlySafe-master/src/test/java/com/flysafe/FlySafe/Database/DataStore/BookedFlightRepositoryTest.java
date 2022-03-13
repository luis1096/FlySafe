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
public class BookedFlightRepositoryTest {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private BookedFlightRepository bookedRepo;
	
	@Before
	public void setUp() throws Exception {
    	entityManagerFactory = Persistence.createEntityManagerFactory("FlySafeDB");
    	entityManager = entityManagerFactory.createEntityManager();
		bookedRepo = new BookedFlightRepository(entityManager);
	}
	
	@After
	public void tearDown() throws Exception {
		bookedRepo = null;
		entityManager = null;
		entityManagerFactory = null;
	}
	
	@Test
	public void testFindById() {
		Optional<BookedFlight> saved = bookedRepo.save(DBPrototype.getBookedFlight());
		assertEquals(true, saved.isPresent());
		
		Optional<BookedFlight> found = bookedRepo.findById(saved.get().getBookingID());
		assertEquals(true, found.isPresent());
		
		Optional<BookedFlight> notFound = bookedRepo.findById(15);
		assertEquals(false, notFound.isPresent());
	}
}
