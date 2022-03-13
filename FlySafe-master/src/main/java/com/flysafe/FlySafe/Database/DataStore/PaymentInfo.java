package com.flysafe.FlySafe.Database.DataStore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * PaymentInfo class models entries in the PaymentInfo table in the database.
 */
@Entity
@Table(name = "payment_infos")
public class PaymentInfo {

    /**
     * Default constructor.
     */
    public PaymentInfo() {
    }
    
    /**
     * Parameterized constructor.
     * 
     * @param cardNum The card number for the PaymentInfo object
     * @param expDate The expiration date for the PaymentInfo object
     */
	public PaymentInfo(String cardNum, String expDate) {
		this.cardNum = cardNum;
		this.expDate = expDate;
	}
    
	/**
	 * (Primary Key) The unique identifier for the PaymentInfo object.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long paymentID;

    /**
     * Card number for the PaymentInfo object.
     */
    @Column(name = "card_num")
    private String cardNum;

    /**
     * The expiration date of the card for the PaymentInfo object.
     */
    @Column(name = "exp_date")
    private String expDate;

    /**
     * The User to whom this PaymentInfo belongs.
     */
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    /**
     * The list of BookedFlight objects corresponding to this PaymentInfo object.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentInfo")
    private List<BookedFlight> bookedFlights = new ArrayList<>();

    /**
     * Getter for paymentID.
     * 
     * @return paymentID
     */
	public long getPaymentID() {
		return paymentID;
	}

	/**
	 * Getter for card number.
	 * 
	 * @return cardNum
	 */
	public String getCardNum() {
		return cardNum;
	}

	/**
	 * Getter for expiration date.
	 * 
	 * @return expDate
	 */
	public String getExpDate() {
		return expDate;
	}

	/**
	 * Getter for the user to whom this PaymentInfo belongs.
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Getter for the list of BookedFlight objects corresponding to this PaymentInfo
	 * 
	 * @return bookedFlights
	 */
	public List<BookedFlight> getBookings() {
		return bookedFlights;
	}
	
	/**
	 * Setter for user.
	 * 
	 * @param user The user to whom this PaymentInfo belongs
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Method to add a BookedFlight to the list of BookedFlights corresponding to this PaymentInfo.
	 * 
	 * @param bookedFlight The BookedFlight object to add to the list
	 */
	public void addBookedFlight(BookedFlight bookedFlight) {
		bookedFlights.add(bookedFlight);
		bookedFlight.setCard(this);
	}

}