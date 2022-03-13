package com.flysafe.FlySafe.Database.DataStore;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

/**
 * User class models entries in the User table in the database.
 */
@Entity
@Table(name = "users")
public class User {

	/**
     * Default constructor.
     */
    public User() {
    }
    
    /**
     * Parameterized constructor.
     * 
     * @param firstName The first name for the User object
     * @param lastName The last name for the User object
     * @param phoneNum The phone number for the User object
     * @param email The email address for the User object
     * @param password The password for the User object
     */
	public User(String firstName, String lastName, String phoneNum, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNum = phoneNum;
		this.email = email;
		this.password = password;
		this.rewardPoints = 0;
		this.twoFactorEnabled = false;
		this.enabled = true;
	}
	
    /**
     * (Primary Key) Unique identifier for User object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userID;

    /**
     * User name for User object (required by Spring Security, not used).
     */
    @Column(name = "username")
    private String userName;
    
    /**
     * The first name of the user
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * The phone number of the user.
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * The email address of the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * The password for the user's account.
     */
    private String password;
    
    /**
     * Whether or not the User is enabled (required by Spring Security).
     */
    private Boolean enabled;

    /**
     * The FlySafe rewards balance for the user.
     */
    @Column(name = "reward_points")
    private int rewardPoints;

	/**
     * Whether or not Two-Factor Authentication is enabled for the user.
     */
    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled;

    /**
     * The list of PaymentInfo objects owned by this user.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<PaymentInfo> paymentInfos = new ArrayList<>();
    
    /**
     * The list of BookedFlight objects corresponding to this user.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<BookedFlight> bookedFlights = new ArrayList<>();
    
    /**
     * The list of roles assigned to this user.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Role> roles;
    
    /**
     * Getter for userID.
     * 
     * @return userID
     */
    public long getUserID() {
		return userID;
	}
    
    /**
     * Getter for userName (required by Spring security). Returns email address.
     * 
     * @return email
     */
    public String getUserName() {
    	return email;
    }
    
    /**
     * Getter for first name.
     * 
     * @return firstName
     */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for last name.
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Getter for phone number.
	 * 
	 * @return phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}

	/**
	 * Getter for email.
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Getter for password.
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Getter for reward points.
	 * 
	 * @return rewardPoints
	 */
	public int getRewardPoints() {
		return rewardPoints;
	}
	
	/**
	 * Getter for Two-Factor enabled setting.
	 * 
	 * @return twoFactorEnabled
	 */
	public Boolean getTwoFactorEnabled() {
		return twoFactorEnabled;
	}
	
	/**
	 * Getter for enabled.
	 * 
	 * @return enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	
	/**
	 * Getter for list of cards belonging to user.
	 * 
	 * @return paymentInfos
	 */
	public List<PaymentInfo> getCards() {
		return paymentInfos;
	}

	/**
	 * Getter for list of flights booked by the user.
	 * 
	 * @return bookedFlights
	 */
	public List<BookedFlight> getBookedFlights() {
		return bookedFlights;
	}
	
	/**
	 * Getter for the list of roles assigned to the user.
	 * 
	 * @return roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * Setter for first name.
	 * 
	 * @param firstName The first name for the User object
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Setter for last name.
	 * 
	 * @param lastName The last name for the User object
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Setter for phone number.
	 * 
	 * @param phoneNum The phone number for the User object
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	/**
	 * Setter for email.
	 * 
	 * @param email The email address for the User object
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Setter for phone number.
	 * 
	 * @param password The phone number for the User object
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Method invoked to update the Two-Factor Authentication settings for the given user.
	 */
	public void updateTwoFactorSettings() {
		if (!twoFactorEnabled)
			twoFactorEnabled = true;
		else
			twoFactorEnabled = false;
	}
	
	/**
	 * Method invoked to add a PaymentInfo object to the user's list of PaymentInfos.
	 * 
	 * @param card The PaymentInfo object to add to the list
	 */
	public void addCard(PaymentInfo card) {
		paymentInfos.add(card);
		card.setUser(this);
	}
	
	/**
	 * Method invoked to add a BookedFlight object to the user's list of BookedFlights.
	 * 
	 * @param booking The BookedFlight object to add to the list
	 */
	public void addBookedFlight(BookedFlight booking) {
		bookedFlights.add(booking);
		booking.setUser(this);
	}
	
	/**
	 * Method invoked to add a Role object to the user's list of Roles.
	 * 
	 * @param role The Role objet to add to the list
	 */
	public void addRole(Role role) {
		roles.add(role);
		role.setUser(this);
	}
	
	/**
	 * Method invoked to add the Reward Points earned from booking a flight to the User's reward points.
	 * 
	 * @param points The number of points to add to the user's total
	 */
	public void addPoints(int points) {
		rewardPoints += points;
	}
	
	/**
	 * Method invoked to decrement the user's Reward Points.
	 * 
	 * @param points The number of points to remove
	 */
	public void removePoints(int points) {
		rewardPoints -= points;
	}	
}