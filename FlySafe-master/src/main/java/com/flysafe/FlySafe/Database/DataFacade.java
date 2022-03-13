package com.flysafe.FlySafe.Database;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.flysafe.FlySafe.Database.DataStore.*;

/**
 * DataFacade acts as an interface through which ApplicationLogic can interact with the database.
 */
public class DataFacade {
	private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private BookedFlightRepository bookedRepo;
    private FlightRepository flightRepo;
    private PaymentInfoRepository cardRepo;
    private RoleRepository roleRepo;
    private UserRepository userRepo;
    private BCryptPasswordEncoder encoder;
    
    /**
     * Default constructor.
     */
    public DataFacade() {
    	entityManagerFactory = Persistence.createEntityManagerFactory("FlySafeDB");
    	entityManager = entityManagerFactory.createEntityManager();
    	bookedRepo = new BookedFlightRepository(entityManager);
    	flightRepo = new FlightRepository(entityManager);
    	cardRepo = new PaymentInfoRepository(entityManager);
    	roleRepo = new RoleRepository(entityManager);
    	userRepo = new UserRepository(entityManager);
    	encoder = new BCryptPasswordEncoder();
    }

    /**
     * Method invoked to add a new User object into the database.
     * Calls the addUser() method in /Database/DataStore/UserList.java to complete the request.
     * 
     * @param firstName The first name for the User object
     * @param lastName The last name for the User object
     * @param email The email address for the User object
     * @param phoneNum The phone number for the User object
     * @param password The password for the User object
     * @return the new User object
     */
    public User addNewUser(String firstName, String lastName, String email, String phoneNum, String password) {
    	String encryptedPass = encoder.encode(password);
        User user = new User(firstName, lastName, phoneNum, email, encryptedPass);
     
        Optional<User> optUser = userRepo.save(user);
        return optUser.isPresent() ? optUser.get() : null;
    }
    
    /**
     * Method invoked to add a newly booked flight to the database.
     * Calls the addBookedFlight() method in Database/DataStore/BookedFlightList.java to process the request.
     * 
     * @param booked The booked flight to be added to the database
     */
    public void addBookedFlight(long userID, Flight flight, String cardNum, String expDate, Boolean savePayment,
    		Boolean maskRequested, Boolean sanitizerRequested, Boolean emptyAdjSeatRequested, int rewardPoints) {
    	
    	Optional<User> optUser = userRepo.findById(userID);
    	if (!optUser.isPresent())
    		return;
    	
    	User user = optUser.get();
    	
    	BookedFlight booking = new BookedFlight(rewardPoints, maskRequested, sanitizerRequested, emptyAdjSeatRequested);
    	bookedRepo.save(booking);
    	

    	PaymentInfo card;
    	Optional<PaymentInfo> optCard;
    	if ((optCard = cardRepo.findByCardNum(cardNum)).isPresent()) {
    		card = optCard.get();
            card.addBookedFlight(booking);
           	cardRepo.save(card);   
    	} else if (savePayment) {
    		card = new PaymentInfo(cardNum, expDate);
            card.addBookedFlight(booking);
           	cardRepo.save(card);      	
           	user.addCard(card); 
    	}
   	
    	user.addBookedFlight(booking);
    	user.addPoints(rewardPoints);
    	userRepo.save(user);   
    	
    	Optional<Flight> optFlight = flightRepo.findByAttributes(flight.getDepAirportCode(), flight.getArrAirportCode(), flight.getDateTime(), flight.getCost());
    	if (optFlight.isPresent()) {
    		optFlight.get().addBookedFlight(booking);
    		flightRepo.save(optFlight.get());
    	} else {
    		flight.addBookedFlight(booking);
    		flightRepo.save(flight);
    	}
    }
    
    /**
     * Method invoked to get the BookedFlight object from the database corresponding to the provided BookingID.
     * 
     * @param email The email address associated with the desired BookedFlight object
     * @param flightID the flightID associated with the desired BookedFlight object
     * @return The BookedFlight corresponding to the provided bookingID
     */
    public BookedFlight getBookedFlight(long bookingID) {
        Optional<BookedFlight> booking = bookedRepo.findById(bookingID);
        return booking.isPresent() ? booking.get() : null;
    }

    /**
     * Method invoked to get a Flight from the database using the flightID.
     * Calls the getFlight() method in /Database/DataStore/FlightList.java to get the requested flight.
     * 
     * @param flightID The flightID for the requested flight
     * @return The Flight object corresponding to the provided flightID
     */
    public Flight getFlight (long flightID) {
        Optional<Flight> flight = flightRepo.findById(flightID);
        
    	return flight.isPresent() ? flight.get() : null;
    }
    
    /**
     * Method invoked to get a User from the database using the email.
     * Calls the getUser() method in /Database/DataStore/UserList.java to get the requested flight.
     * 
     * @param email The email address for the requested flight
     * @return The User object corresponding to the provided email address
     */
    public User getUser(long userID) {
    	Optional<User> user = userRepo.findById(userID);
    	return user.isPresent() ? user.get() : null;
    }
    
    /**
     * Method invoked to update the Two-Factor Authentication settings for the logged in user.
     * Calls the updateTwoFactorAuthSettings in /Database/DataStore/UserList.java to process the request.
     * 
     * @param user The email address for the user whose settings for Two-Factor Authentication will be updated
     */
    public void updateTwoFactorSettings(long userID) {
        Optional<User> optUser = userRepo.findById(userID);
        if (!optUser.isPresent())
        	return;
        
        optUser.get().updateTwoFactorSettings();       
        userRepo.save(optUser.get());
    }
    
    /**
     * Method invoked to update the COVID-19 settings for the provided bookingID.
     * Calls the updateTwoFactorAuthSettings in /Database/DataStore/UserList.java to process the request.
     * 
     * @param user The bookingID for the booked flight that will be updated
     */
    public void updateCOVIDSettings(long bookingID, Boolean maskReq, Boolean sanitizerReq, Boolean emptySeatReq) {
    	Optional<BookedFlight> booking = bookedRepo.findById(bookingID);
    	if (!booking.isPresent())
    		return;
    	
    	BookedFlight booked = booking.get();
    	booked.setMaskRequested(maskReq);
    	booked.setSanitizerRequested(sanitizerReq);
    	booked.setEmptyAdjSeatRequested(emptySeatReq);
    	
    	bookedRepo.save(booked);
    }

}
