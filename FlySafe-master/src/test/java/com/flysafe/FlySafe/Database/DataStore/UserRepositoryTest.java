package com.flysafe.FlySafe.Database.DataStore;

import static org.junit.Assert.*;
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
public class UserRepositoryTest {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private UserRepository userRepo;
	
	@Before
	public void setUp() throws Exception {
    	entityManagerFactory = Persistence.createEntityManagerFactory("FlySafeDB");
    	entityManager = entityManagerFactory.createEntityManager();
		userRepo = new UserRepository(entityManager);
	}
	
	@After
	public void tearDown() throws Exception {
		userRepo = null;
		entityManager = null;
		entityManagerFactory = null;
	}
	
	@Test
	public void testFindById() {
		Optional<User> saved = userRepo.save(DBPrototype.getUser());
		assertEquals(true, saved.isPresent());
		
		Optional<User> found = userRepo.findById(saved.get().getUserID());
		assertEquals(true, found.isPresent());
		
		Optional<User> notFound = userRepo.findById(15);
		assertEquals(false, notFound.isPresent());
	}
	
	@Test
	public void testFindByEmail() {
		Optional<User> saved = userRepo.save(DBPrototype.getUser());
		assertEquals(true, saved.isPresent());
		
		Optional<User> found = userRepo.findByEmail(saved.get().getEmail());
		assertEquals(true, found.isPresent());
		
		Optional<User> notFound = userRepo.findByEmail("iliketurtles@hotmail.com");
		assertEquals(false, notFound.isPresent());
	}
}
