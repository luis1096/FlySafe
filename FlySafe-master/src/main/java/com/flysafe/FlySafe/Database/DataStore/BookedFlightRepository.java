package com.flysafe.FlySafe.Database.DataStore;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to manage interactions with the BookedFlight table in the database.
 */
public class BookedFlightRepository {
	
	/**
	 * EntityManager for the FlySafe system.
	 */
	private EntityManager entityManager;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param entityManager The EntityManager for the FlySafe System
	 */
	public BookedFlightRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * Method invoked to find a BookedFlight object given its bookingID.
	 * 
	 * @param id The bookingID of the BookedFlight object
	 * @return The BookedFlight object corresponding to the id
	 */
    public Optional<BookedFlight> findById(long id) {
        BookedFlight booking = entityManager.find(BookedFlight.class, id);
        return booking != null ? Optional.of(booking) : Optional.empty();
    }
    
    /**
     * Method invoked to save a BookedFlight object to the database.
     * 
     * @param booking the BookedFlight object to be saved
     * @return An Optional BookedFlight object corresponding to the BookedFlight object that was saved
     */
    public Optional<BookedFlight> save(BookedFlight booking) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(booking);
            entityManager.getTransaction().commit();
            return Optional.of(booking);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
