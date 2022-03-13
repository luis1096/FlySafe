package com.flysafe.FlySafe.Database.DataStore;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to manage interactions with the Role table in the database.
 */
public class RoleRepository {
	
	/**
	 * EntityManager for the FlySafe system.
	 */
	private EntityManager entityManager;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param entityManager The EntityManager for the FlySafe System
	 */
	public RoleRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
		
	/**
	 * Method invoked to find a Role object given its roleID.
	 * 
	 * @param id The roleID of the Role object
	 * @return The Role object corresponding to the id
	 */
    public Optional<Role> findById(long id) {
        Role role = entityManager.find(Role.class, id);
        return role != null ? Optional.of(role) : Optional.empty();
    }
    
	/**
	 * Method invoked to find a Role object given its name.
	 * 
	 * @param name The name of the Role object
	 * @return The Role object corresponding to the name
	 */
    public Optional<Role> findByName(String name) {
        List<Role> roles = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class).setParameter("name", name).getResultList();
        return !roles.isEmpty() ? Optional.of(roles.get(0)) : Optional.empty();
    }
    
    /**
     * Method invoked to save a Role object to the database.
     * 
     * @param role the Role object to be saved
     * @return An Optional Role object corresponding to the Role object that was saved
     */
    public Optional<Role> save(Role role) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(role);
            entityManager.getTransaction().commit();
            return Optional.of(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
}
