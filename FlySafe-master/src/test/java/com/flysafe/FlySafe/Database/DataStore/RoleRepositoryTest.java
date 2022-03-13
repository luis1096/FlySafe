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
public class RoleRepositoryTest {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private RoleRepository roleRepo;
	
	@Before
	public void setUp() throws Exception {
    	entityManagerFactory = Persistence.createEntityManagerFactory("FlySafeDB");
    	entityManager = entityManagerFactory.createEntityManager();
		roleRepo = new RoleRepository(entityManager);
	}
	
	@After
	public void tearDown() throws Exception {
		roleRepo = null;
		entityManager = null;
		entityManagerFactory = null;
	}
	
	@Test
	public void testFindById() {
		Optional<Role> saved = roleRepo.save(DBPrototype.getRole());
		assertEquals(true, saved.isPresent());
		
		Optional<Role> found = roleRepo.findById(saved.get().getRoleID());
		assertEquals(true, found.isPresent());
		
		Optional<Role> notFound = roleRepo.findById(15);
		assertEquals(false, notFound.isPresent());
	}
	
	@Test
	public void testFindByName() {
		Optional<Role> saved = roleRepo.save(DBPrototype.getRole());
		assertEquals(true, saved.isPresent());
		
		Optional<Role> found = roleRepo.findByName(saved.get().getName());
		assertEquals(true, found.isPresent());
		
		Optional<Role> notFound = roleRepo.findByName("FAILER");
		assertEquals(false, notFound.isPresent());
	}
}
