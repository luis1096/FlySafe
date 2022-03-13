package com.flysafe.FlySafe.Database.DataStore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * BookedFlight class models entries in the BookedFlight table in the database.
 */
@Entity
@Table(name = "booked_flights")
public class BookedFlight {

    /**
     * Default constructor.
     */
	public BookedFlight() {
		
	}

	/**
	 * Parameterized constructor.
	 * 
	 * @param rewardPoints The reward points for this BookedFlight
	 * @param maskRequested Whether or not a mask was requested
	 * @param sanitizerRequested Whether or not sanitizer was requested
	 * @param emptyAdjSeatRequested Whether or not an empty adjacent seat was requested
	 */
    public BookedFlight(int rewardPoints, Boolean maskRequested, Boolean sanitizerRequested, Boolean emptyAdjSeatRequested) {
    	this.rewardPoints = rewardPoints;
    	this.maskRequested = maskRequested;
    	this.sanitizerRequested = sanitizerRequested;
    	this.emptyAdjSeatRequested = emptyAdjSeatRequested;
    }
    
    /**
     * (Primary Key) Unique reservation ID to identify BookedFlight.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private long bookingID;

    /**
     * The User associated with BookedFlight.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The Flight associated with BookedFlight.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    /**
     * The PaymentInfo associated with BookedFlight.
     */
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentInfo paymentInfo;

    /**
     * The number of FlySafe Rewards points earned from this BookedFlight.
     */
    @Column(name = "reward_points")
    private int rewardPoints;

    /**
     * Whether or not a mask was requested for this BookedFlight.
     */
    @Column(name = "mask_req")
    private Boolean maskRequested;

    /**
     * Whether or not sanitizer was requested for this BookedFlight.
     */
    @Column(name = "san_req")
    private Boolean sanitizerRequested;

    /**
     * Whether or not empty adjacent seat(s) were requested for this BookedFlight.
     */
    @Column(name = "empty_seat_req")
    private Boolean emptyAdjSeatRequested;

    /**
     * Getter for bookingID.
     * 
     * @return bookingID
     */
	public long getBookingID() {
		return bookingID;
	}
	
	/**
	 * Getter for user.
	 * 
	 * @return User corresponding to this BookedFlight
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Getter for flight.
	 * 
	 * @return Flight corresponding to this BookedFlight
	 */
	public Flight getFlight() {
		return flight;
	}
	
	/**
	 * Getter for paymentInfo.
	 * 
	 * @return PaymentInfo corresponding to this BookedFlight (can be null)
	 */
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	
	/**
	 * Getter for rewardPoints.
	 * 
	 * @return rewardPoints
	 */
	public int getRewardPoints() {
		return rewardPoints;
	}
	
	/**
	 * Getter for maskRequested.
	 * 
	 * @return maskRequested
	 */
	public Boolean getMaskRequested() {
		return maskRequested;
	}
	
	/**
	 * Getter for sanitizerRequested.
	 * 
	 * @return sanitizerRequested
	 */
	public Boolean getSanitizerRequested() {
		return sanitizerRequested;
	}
	
	/**
	 * Getter for emptyAdjSeatRequested.
	 * 
	 * @return emptyAdjSeatRequested
	 */
	public Boolean getEmptyAdjSeatRequested() {
		return emptyAdjSeatRequested;
	}
	
	/**
	 * Setter for user.
	 * 
	 * @param user The User corresponding to this BookedFlight
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Setter for flight.
	 * 
	 * @param flight The Flight corresponding to this BookedFlight
	 */
	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	
	/**
	 * Setter for paymentInfo.
	 * 
	 * @param paymentInfo The PaymentInfo corresponding to this BookedFlight
	 */
	public void setCard(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
	/**
	 * Setter for rewardPoints.
	 * 
	 * @param rewardPoints The number of reward points for this BookedFlight
	 */
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	/**
	 * Setter for maskRequested.
	 * 
	 * @param maskRequested Whether or not a mask was requested
	 */
	public void setMaskRequested(Boolean maskRequested) {
		this.maskRequested = maskRequested;
	}
	
	/**
	 * Setter for sanitizerRequested.
	 * 
	 * @param sanitizerRequested Whether or not sanitizer was requested
	 */
	public void setSanitizerRequested(Boolean sanitizerRequested) {
		this.sanitizerRequested = sanitizerRequested;
	}
	
	/**
	 * Setter for emtpyAdjSeatRequested.
	 * 
	 * @param emptyAdjSeatRequested Whether or not an empty adjacent seat was requested
	 */
	public void setEmptyAdjSeatRequested(Boolean emptyAdjSeatRequested) {
		this.emptyAdjSeatRequested = emptyAdjSeatRequested;
	}
}