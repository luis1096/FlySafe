package com.flysafe.FlySafe.Database.DataStore;

import static org.junit.Assert.assertEquals;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class FlightRepositoryTest {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private FlightRepository flightRepo;
	
	@Before
	public void setUp() throws Exception {
    	entityManagerFactory = Persistence.createEntityManagerFactory("FlySafeDB");
    	entityManager = entityManagerFactory.createEntityManager();
		flightRepo = new FlightRepository(entityManager);
	}
	
	@After
	public void tearDown() throws Exception {
		flightRepo = null;
		entityManager = null;
		entityManagerFactory = null;
	}
	
	@Test
	public void testFindById() throws Exception {
		Optional<Flight> saved = flightRepo.save(DBPrototype.getFlight());
		assertEquals(true, saved.isPresent());
		
		Optional<Flight> found = flightRepo.findById(saved.get().getFlightID());
		assertEquals(true, found.isPresent());
		
		Optional<Flight> notFound = flightRepo.findById(15);
		assertEquals(false, notFound.isPresent());		
	}
	
	@Test
	public void testFindByAttributes() throws Exception {
		Optional<Flight> saved = flightRepo.save(DBPrototype.getFlight());
		assertEquals(true, saved.isPresent());

		Flight flight = saved.get();
		Optional<Flight> found = flightRepo.findByAttributes(flight.getDepAirportCode(), flight.getArrAirportCode(), flight.getDateTime(), flight.getCost());
		assertEquals(true, found.isPresent());
		
		Date date = new SimpleDateFormat("MM/dd/yyyy").parse("12/23/2020");
		Optional<Flight> notFound = flightRepo.findByAttributes("SEA", "DAL", date, (float)100.00);
		assertEquals(false, notFound.isPresent());
		
	}
}
