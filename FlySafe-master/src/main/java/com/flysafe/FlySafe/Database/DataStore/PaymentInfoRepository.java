package com.flysafe.FlySafe.Database.DataStore;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to manage interactions with the PaymentInfo table in the database.
 */
public class PaymentInfoRepository {
	
	/**
	 * EntityManager for the FlySafe system.
	 */
	private EntityManager entityManager;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param entityManager The EntityManager for the FlySafe System
	 */
	public PaymentInfoRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
		
	/**
	 * Method invoked to find a PaymentInfo object given its paymentID.
	 * 
	 * @param id The paymentID of the PaymentInfo object
	 * @return The PaymentInfo object corresponding to the id
	 */
    public Optional<PaymentInfo> findById(long id) {
        PaymentInfo card = entityManager.find(PaymentInfo.class, id);
        return card != null ? Optional.of(card) : Optional.empty();
    }
    
    /**
     * Method invoked to find a PaymentInfo object given its card number.
     * 
     * @param cardNum The card number of the PaymentInfo object
     * @return The PaymentInfo object corresponding to the card number
     */
    public Optional<PaymentInfo> findByCardNum(String cardNum) {
        List<PaymentInfo> cards = entityManager.createQuery("SELECT c FROM PaymentInfo c WHERE c.cardNum = :card_num", PaymentInfo.class).setParameter("card_num", cardNum).getResultList();
        return !cards.isEmpty() ? Optional.of(cards.get(0)) : Optional.empty();
    }
    
    /**
     * Method invoked to save a PaymentInfo object to the database.
     * 
     * @param card the PaymentInfo object to be saved
     * @return An Optional PaymentInfo object corresponding to the PaymentInfo object that was saved
     */
    public Optional<PaymentInfo> save(PaymentInfo card) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(card);
            entityManager.getTransaction().commit();
            return Optional.of(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
   
}
