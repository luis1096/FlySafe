package com.flysafe.FlySafe.Application;

import com.flysafe.FlySafe.Database.*;
import com.flysafe.FlySafe.Database.DataStore.*;
import com.flysafe.FlySafe.Security.FlySafeUserDetails;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * ApplicationLogic handles the logic for the FlySafe System.
 * It interacts with /Presentation/PresentationLogic.java to send information to the end users and
 * interacts with /Database/DataFacade.java to access the database.
 */
public class ApplicationLogic {	
	/**
	 * Facade object to be used for database interactions.
	 */
	private DataFacade facade;
	
	/**
	 * Static instance variable to be used for Singleton design pattern.
	 */
	static private ApplicationLogic _instance = null;
	
	/**
	 * HashMap to store most recent search results for each user of the system.
	 */
	private HashMap<Long, List<Flight>> resultsMap; 
	
    /**
     * Default constructor set to private to allow Singleton design pattern.
     */
    private ApplicationLogic() {
    	facade = new DataFacade();
    	resultsMap = new HashMap<>();
    }
    
    /**
     * Method to obtain Singleton instance of ApplicationLogic class.
     * 
     * @return The Singleton instance of ApplicationLogic
     */
     public static ApplicationLogic instance() {
    	if (_instance == null)
    		_instance = new ApplicationLogic();
    	
    	return _instance;
    }

    /**
     * Method invoked to create a new User object with parameters provided by the user.
     * Calls addNewUser() method in /Database/DataFacade.java to add the new User object to the database.
     * 
     * @param firstName The first name for the User object
     * @param lastName The last name for the User object
     * @param email The email address for the User object
     * @param phoneNum The phone number for the User object
     * @param password The password for the User object
     * @return The user object created by this method
     */
    public void createAccount(String firstName, String lastName, String email, String phoneNum, String password) {
        facade.addNewUser(firstName, lastName, email, phoneNum, password);
    }
    
    /**
     * Method invoked to retrieve User corresponding to provided UserID from the database.
     * 
     * @param userID The userID of the User to be retrieved
     * @return The User corresponding to the provided userID
     */
    public User getUser(long userID) {
    	return facade.getUser(userID);
    }

    /**
     * Method invoked to search for flights matching user-provided criteria.
     * Calls SkyScanner application to obtain results and caches results in resultsMap.
     * 
     * @param source The source location for the search
     * @param destination The destination location for the search
     * @param date The date for the search
     * @return The list of flights matching the search criteria
     */
    public List<Flight> searchFlights(String source, String destination, String date) {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        JSONObject jObj;
        JSONArray jArr;
        String srcCode = "", destCode = "";
        
        try {
    		long userID = getLoggedInUserID();
    		
    		if (resultsMap.containsKey(userID))
    			resultsMap.get(userID).clear();
    		else
    			resultsMap.put(userID, new ArrayList<>());
    		
        	request = new Request.Builder()
        		.url("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/US/USD/en-US/?query=" + source)
        		.get()
        		.addHeader("x-rapidapi-key", RAPIDAPI_KEY)
        		.addHeader("x-rapidapi-host", RAPIDAPI_HOST)
        		.build();
               
        	response = client.newCall(request).execute();
        	if (!response.isSuccessful())
        		return resultsMap.get(userID);
 
        	jObj = new JSONObject(response.body().string());
        	response.close();   
        	
        	if ((jArr = jObj.getJSONArray("Places")).length() == 0)
        		return resultsMap.get(userID);
        	
        	srcCode = jArr.getJSONObject(0).getString("PlaceId");       	     	
        	if (srcCode == "")
        		return resultsMap.get(userID);
        	
            request = new Request.Builder()
            		.url("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/US/USD/en-US/?query=" + destination)
            		.get()
            		.addHeader("x-rapidapi-key", RAPIDAPI_KEY)
            		.addHeader("x-rapidapi-host", RAPIDAPI_HOST)
            		.build();
        	
        	response = client.newCall(request).execute();
        	if (!response.isSuccessful())
        		return resultsMap.get(userID);
 
        	jObj = new JSONObject(response.body().string());
        	response.close();
        	
        	if ((jArr = jObj.getJSONArray("Places")).length() == 0)
        		return resultsMap.get(userID);
        	
        	destCode = jArr.getJSONObject(0).getString("PlaceId");         	
        	if (destCode == "")
        		return resultsMap.get(userID);
        	
        	 request = new Request.Builder()
        				.url("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/US/USD/en-US/"
        						+ srcCode + "/" + destCode + "/" + date)
        				.get()
        				.addHeader("x-rapidapi-key", RAPIDAPI_KEY)
        				.addHeader("x-rapidapi-host", RAPIDAPI_HOST)
        				.build();
        	 
         	response = client.newCall(request).execute();
         	if (!response.isSuccessful())
         		return resultsMap.get(userID);
  
         	jObj = new JSONObject(response.body().string());
         	response.close();
         	
         	HashMap<Integer, String> carrierMap = new HashMap<>();
         	jArr = jObj.getJSONArray("Carriers");
         	for (int i = 0; i < jArr.length(); i++) {
         		JSONObject carrier = jArr.getJSONObject(i);
         		carrierMap.put(carrier.getInt("CarrierId"), carrier.getString("Name"));
         	}
         	
         	HashMap<Integer, String[]> placeMap = new HashMap<>(); 
         	jArr = jObj.getJSONArray("Places");
         	for (int i = 0; i < jArr.length(); i++) {
         		JSONObject place = jArr.getJSONObject(i);
         		if (place.has("CityName")) {
         			String[] details = { place.getString("SkyscannerCode"),
         								 place.getString("CityName") };
         			placeMap.put(place.getInt("PlaceId"), details);
         		}
         	}         	
         	
         	jArr = jObj.getJSONArray("Quotes");
         	for (int i = 0; i < jArr.length(); i++) {
         		JSONObject quote = jArr.getJSONObject(i);
         		float cost = Float.parseFloat(quote.getString("MinPrice"));
         		JSONObject outbound = quote.getJSONObject("OutboundLeg");
         		Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(outbound.getString("DepartureDate").replace('T', ' '));
         		String airline = carrierMap.get(outbound.getJSONArray("CarrierIds").getInt(0));
         		String[] place = placeMap.get(outbound.get("OriginId"));
         		String depCode = place[0];
         		String depCity = place[1];
         		place = placeMap.get(outbound.get("DestinationId"));
         		String arrCode = place[0];
         		String arrCity = place[1];
         		Flight flight = new Flight(airline, dateTime, cost, depCity, depCode, arrCity, arrCode);
         		resultsMap.get(userID).add(flight);
         	}
         	
         	return resultsMap.get(userID);
        }
    	catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * Method to obtain the results for the most recent flight search performed by the logged in user.
     * 
     * @param userID The userID of the user for which the list of flights is to be obtained
     * @return The list of flights matching the search criteria of the most recent search
     */
    public List<Flight> getSearchResults(long userID) {
    	List<Flight> results = new ArrayList<>();
    	if (resultsMap.containsKey(userID))
    		results = resultsMap.get(userID);
    	
    	return results;
    }
    
    /**
     * Method invoked to book a selected flight for the logged-in User.
     * 
     * @param flightID The flightID for the flight to be booked
     * @param cardNum The card number to be used for the payment
     * @param expDate The expiration date of the card
     * @param secCode The security code for the card
     * @param savePayment Whether or not the User has chosen to save their payment information
     * @param maskRequested Whether or not a mask was requested
     * @param sanitizerRequested Whether or not sanitizer was requested
     * @param emptyAdjSeatRequested Whether or not an empty adjacent seat was requested 
     */
    public void bookFlight(long userID, int index, String cardNum, String expDate, String secCode, Boolean savePayment,
    		Boolean maskRequested, Boolean sanitizerRequested, Boolean emptyAdjSeatRequested) {
    	
    	processPayment(cardNum, expDate, secCode);
    	Flight flight = getFlight(index);
    	
    	if (flight == null)
    		return;
    	
    	int rewards = (int)(flight.getCost() * 10);
    	
    	facade.addBookedFlight(userID, flight, cardNum, expDate, savePayment, maskRequested, sanitizerRequested, emptyAdjSeatRequested, rewards);
    }

    /**
     * Method invoked to process a payment for a booked flight.
     * 
     * @param cardNum The card number to be used for the payment
     * @param expDate The expiration date of the card
     * @param secCode The security code for the card
     */
    public void processPayment(String cardNum, String expDate, String secCode) {
        // TODO Pretend I am implemented :)
    }

    /**
     * Method invoked to get the Flight object for a flight selected by the user.
     * Locates and returns the Flight from the results list, and then from the database
     * if it is not in the results list.
     * 
     * @param flightID the flightID for the flight being requested
     * @return The Flight corresponding to the provided flightID
     */
    public Flight getFlight(int index) {		
    	return getFlightFromResults(index);
    }
    
    /**
     * Method invoked to get a BookedFlight object from the database using its bookingID.
     * 
     * @param bookingID The bookingID of the BookedFlight object
     * @return The BookedFlight object corresponding to the provided bookingID
     */
    public BookedFlight getBookedFlight(long bookingID) {
    	return facade.getBookedFlight(bookingID);
    }
    
    /**
     * Method invoked to update the COVID options selected for a given booked flight.
     * 
     * @param bookingID The bookingID of the booked flight to be updated.
     * @param maskReq Whether or not a mask was requested.
     * @param sanitizerReq Whether or not sanitizer was requested.
     * @param emptySeatReq Whether or not an empty adjacent seat was requested.
     */
    public void updateCOVIDOptions(long bookingID, Boolean maskReq, Boolean sanitizerReq, Boolean emptySeatReq) {
    	facade.updateCOVIDSettings(bookingID, maskReq, sanitizerReq, emptySeatReq);
    }

    /**
     * Method invoked to enable Two-Factor Authentication for their account.
     * Calls the updateTwoFactorSetting() in DataFacade to process the request.
     * 
     * @param email The email address of the account for which the Two-Factor Authentication settings will be changed.
     */
    public void updateTwoFactorSettings(long userID) {
    	facade.updateTwoFactorSettings(userID);
    }

    /**
     * Method invoked to send a text message containing a code to the user's provided phone number for Two-Factor Authentication.
     * 
     * @return The code generated and sent to the user.
     */
    public String sendTwoFactorConfirmationText(String phoneNum) {
    	Twilio.init(TWILIO_SID, TWILIO_TOK);
    	String recepient = (phoneNum.length() == 10) ? "+1" + phoneNum : "+" + phoneNum;
    	String code = generateCode();
    	
    	Message message = Message.creator(
    	    new PhoneNumber(recepient),
    	    new PhoneNumber(TWILIO_NUM),
    	    "Your Authentication Code: " + code)
    	.create();
    	  	
        return code; 
    }

   /**
   * Method to validate Two-factor authentication.
   *  
   * @param code The code sent to the user form the system
   * @param input The code entered by the user
   * @return True if the code matches the input, else false
   */
    public Boolean validateTwoFactorAuth(String code, String input) {
    	return input.compareTo(code) == 0;
    }
    
    /**
     * Method to obtain the userID of the user logged into the system.
     * 
     * @return The userID of the user logged into the system
     */
    public long getLoggedInUserID() {
		FlySafeUserDetails userDetails = (FlySafeUserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return userDetails.getUser().getUserID();
    }
    
    /**
     * Helper method to get the desired flight from the search results.
     * 
     * @param index The index of the desired flight
     * @return The flight object corresponding to the provided index
     */
    public Flight getFlightFromResults(int index) {
		long userID = getLoggedInUserID();
		
		if (!resultsMap.containsKey(userID))
			return null;
		else if (index < 0 || index >= resultsMap.get(userID).size())
			return null;
		
		return resultsMap.get(userID).get(index);
    }
    
    /**
     * Helper method to generate code for Two-Factor Authentication.
     * 
     * @return The code for Two-Factor Authentication
     */
    private String generateCode() {
    	Random rand = new Random();
    	String code = "";
    	
    	for (int i = 0; i < 6; i++)
    		code += rand.nextInt(10);
 
    	return code;
    }

	private static final String RAPIDAPI_KEY = "5afb1b441amsh7f195839bdf507dp1977d5jsn07f8ca380408";
	private static final String RAPIDAPI_HOST = "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
	private static final String TWILIO_SID = "AC59507f4631b7642817ff2b2226aeb0c6";
	private static final String TWILIO_TOK = "47b75013b907ef562b2a761ca8348a45";
	private static final String TWILIO_NUM = "+12525651361";
}
