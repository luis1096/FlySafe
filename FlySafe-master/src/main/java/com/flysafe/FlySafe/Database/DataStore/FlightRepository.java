package com.flysafe.FlySafe.Database.DataStore;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to manage interactions with the Flight table in the database.
 */
public class FlightRepository {
	
	/**
	 * EntityManager for the FlySafe system.
	 */
	private EntityManager entityManager;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param entityManager The EntityManager for the FlySafe System
	 */
	public FlightRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
		
	/**
	 * Method invoked to find a Flight object given its flightID.
	 * 
	 * @param id The flightID of the Flight object
	 * @return The Flight object corresponding to the id
	 */
    public Optional<Flight> findById(long id) {
        Flight flight = entityManager.find(Flight.class, id);
        return flight != null ? Optional.of(flight) : Optional.empty();
    }
    
	/**
	 * Method invoked to find a Flight object given some of its attributes.
	 * 
	 * @param depCode The departure airport code for the Flight object
	 * @param arrCode The arrival airport code for the Flight object
	 * @param dateTime The date/time for the Flight object
	 * @param cost The cost for the Flight object
	 * @return An optional containing the Flight if found, or an empty optional otherwise
	 */
    public Optional<Flight> findByAttributes(String depCode, String arrCode, Date dateTime, float cost) {
    	 List<Flight> flights = entityManager.createQuery("SELECT f FROM Flight f WHERE f.depAirportCode = :dep_code AND f.arrAirportCode = :arr_code AND f.dateTime = :date_time AND f.cost = :cost", Flight.class)
    			 .setParameter("dep_code", depCode)
    			 .setParameter("arr_code", arrCode)
    			 .setParameter("date_time", dateTime)
    			 .setParameter("cost", cost).getResultList();
         return !flights.isEmpty() ? Optional.of(flights.get(0)) : Optional.empty();
    }
    
    /**
     * Method invoked to save a Flight object to the database.
     * 
     * @param flight the Flight object to be saved
     * @return An Optional Flight object corresponding to the Flight object that was saved
     */
    public Optional<Flight> save(Flight flight) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flight);
            entityManager.getTransaction().commit();
            return Optional.of(flight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
