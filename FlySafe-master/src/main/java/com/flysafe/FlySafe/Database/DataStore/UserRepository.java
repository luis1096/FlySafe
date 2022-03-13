package com.flysafe.FlySafe.Database.DataStore;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to manage interactions with the User table in the database.
 */
@Component("userRepo")
public class UserRepository {	
	
	/**
	 * EntityManager for the FlySafe system.
	 */
	private EntityManager entityManager;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param entityManager The EntityManager for the FlySafe System
	 */
	public UserRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
		
	/**
	 * Method invoked to find a User object given its userID.
	 * 
	 * @param id The userID of the User object
	 * @return The User object corresponding to the id
	 */
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();
    }
    
	/**
	 * Method invoked to find a User object given its email address.
	 * 
	 * @param email The email address of the User object
	 * @return The User object corresponding to the email address
	 */
    public Optional<User> findByEmail(String email) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getResultList();
        return !users.isEmpty() ? Optional.of(users.get(0)) : Optional.empty();
    }
    
    /**
     * Method invoked to save a User object to the database.
     * 
     * @param user the User object to be saved
     * @return An Optional User object corresponding to the User object that was saved
     */
    public Optional<User> save(User user) {
        try {
        	entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
