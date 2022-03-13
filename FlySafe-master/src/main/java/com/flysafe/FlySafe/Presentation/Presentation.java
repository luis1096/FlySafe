package com.flysafe.FlySafe.Presentation;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.flysafe.FlySafe.Database.DataStore.*;
import com.flysafe.FlySafe.Application.*;
import com.flysafe.FlySafe.Security.*;

/**
 * The Presentation class is responsible for navigating users to the various web pages as well as 
 * handling form submissions and passing the information along to the ApplicationLogic class.
 */
@Controller
public class Presentation {
	
	/**
	 * ApplicationLogic instance.
	 */
	private ApplicationLogic logic;

	/**
	 * Default constructor, gets instance of ApplicationLogic.
	 */
	public Presentation() {
		logic = ApplicationLogic.instance();
	}
	
	/**
	 * Method invoked to display the home page for a logged-in user.
	 * 
	 * @param model The model to allow the User information to be displayed in the web page
	 * @return The path to the Registered User home page
	 */
	@RequestMapping("/home")
	public String displayRegUserHomePage(Model model) {		
		long userID = logic.getLoggedInUserID();		
		User user = logic.getUser(userID);
		model.addAttribute("user", user);
		
		return "/UserPage/home";
	}
	/**
	 * Method invoked to display the login form to the user.
	 * 
	 * @return The path to the Page containing the login form
	 */
	@GetMapping("/login")
	public String displayLoginForm() {
		return "/UserPage/login";
	}
	
	/**
	 * Method invoked to handle the submission of the login form. The login process is handled by Spring Security, so this 
	 * method either redirects the user to the Two-Factor authentication form if the user has Two-Factor Authentication 
	 * enabled, or to the registered user home page if not.
	 * 
	 * @param model The model to allow user information to be displayed in the web page
	 * @return The path to the web page which the user will be redirected to
	 */
	@GetMapping("/submitLoginForm")
	public String submitLoginForm(Model model) {
		long userID = logic.getLoggedInUserID();	
		User user = logic.getUser(userID);
			
		if (user.getTwoFactorEnabled())
			return "redirect:/authenticate";
		
		
		return "redirect:/home";
	}
	
	/**
	 * Method invoked to display the Create Account form to the User.
	 * 
	 * @return The path to the web page with the Create Account form
	 */
	@RequestMapping("/createaccount")
	public String displayCreateAccountForm() {
		return "/UserPage/createaccount";
	}
	
	/**
	 * Method invoked to handle submission of the Create Account form. Sends the information provided 
	 * by the user along to the ApplicationLogic layer to complete the account creation process.
	 * 
	 * @param firstName The first name provided by the user
	 * @param lastName The last name provided by the user
	 * @param phoneNum The phone number provided by the user
	 * @param email The email address provided by the user
	 * @param password The password for the account to be created
	 * @return The path to the Registered User home page
	 */
	@PostMapping("/submitCreateAccountForm")
	public String submitCreateAccountForm(@RequestParam("fname") String firstName, @RequestParam("lname") String lastName,
			@RequestParam("pnum") String phoneNum, @RequestParam("email") String email, @RequestParam("password") String password) {
		logic.createAccount(firstName, lastName, email, phoneNum, password);
		
		return "redirect:/home";
	}
	
	/**
	 * Method invoked to display the Two-Factor Authentication form to the user.
	 * 
	 * @param model The model to allow user information to be displayed in the web page
	 * @return The path to the web page with the authentication form
	 */
	@RequestMapping("/authenticate")
	public String displayTwoFactorAuthForm(Model model) {
		long userID = logic.getLoggedInUserID();	
		User user = logic.getUser(userID);
		
		String authCode = logic.sendTwoFactorConfirmationText(user.getPhoneNum());
		model.addAttribute("authCode", authCode);
		model.addAttribute("user", user);
		return "/UserPage/authenticate";
	}
	
	/**
	 * Method invoked to handle the submission of the Two-Factor Authentication form. Passes the information to the 
	 * ApplicationLogic to get validation results.
	 * 
	 * @param model The model to display user information in the web page
	 * @param authCode The authorization code provided by the system to the user
	 * @param code The code provided by the user
	 * @return The path to the Registered User home page if authentication succeeds, or the path to an error page if authentication failed
	 */
	@PostMapping("/submitTwoFactorForm")
	public String submitTwoFactorAuthForm(Model model, @RequestParam("authCode") String authCode, @RequestParam("code") String code) {
		if(!logic.validateTwoFactorAuth(authCode, code)) {
			return "redirect:/autherror?authfail=true";
		}		
		
		return "redirect:/home";
	}
	
	/**
	 * Method invoked when Two-Factor authentication fails, redirects the user to the Authentication Error page.
	 * 
	 * @param model The model to display user information in the web page
	 * @param authFail Whether or not the Two-Factor Authentication failed
	 * @return The path to the Authentication Error page
	 */
	@GetMapping("/autherror")
	public String displayAuthErrorPage(Model model, @RequestParam("authfail") Boolean authFail) {
		if (authFail)
			model.addAttribute("authfail", true);
		
		return "/UserPage/autherror";
	}
	
	/**
	 * Method invoked when a logged-in user clicks on the View Profile button. Obtains the User information from the 
	 * database and redirects the user to their profile page.
	 * 
	 * @param model The model to display the user information in the web page
	 * @return The path to the My Profile page
	 */
	@RequestMapping("/myprofile")
	public String displayProfilePage(Model model) {
		long userID = logic.getLoggedInUserID();
		User user = logic.getUser(userID);
		List<BookedFlight> bookings = user.getBookedFlights();
		List<PaymentInfo> cards = user.getCards();
		model.addAttribute("user", user);
		model.addAttribute("bookings", bookings);
		model.addAttribute("cards", cards);
		
		return "/UserPage/myprofile";
	}
	
	/**
	 * Method invoked when the logged-in user clicks on the View Account Settings link. Obtains the user information 
	 * from the database and redirects the user to their Account Settings page.
	 * 
	 * @param model The model to display the user information in the web page
	 * @return The path to the Account Settings page
	 */
	@RequestMapping("/accountsettings")
	public String displayAccountSettingsPage(Model model) {
		long userID = logic.getLoggedInUserID();		
		User user = logic.getUser(userID);		
		model.addAttribute("user", user);
		
		return "/UserPage/accountsettings";
	}
	
	/**
	 * Method invoked when the logged-in user clicks on the View Details button for one of their booked flights. Gets the information 
	 * about the booking from the database and redirects the user to the View Booked Flight page.
	 * 
	 * @param model The model to display the booking information in the web page
	 * @param bookingID The bookingID of the BookedFlight for which the user requested the details.
	 * @return The path to the View Booked Flight page
	 */
	@GetMapping("/viewbooking")
	public String displayCOVIDOptionsPage(Model model, @RequestParam("id") long bookingID) {
		BookedFlight booking = logic.getBookedFlight(bookingID);
		Flight flight = booking.getFlight();
		model.addAttribute("booking", booking);
		model.addAttribute("flight", flight);
		
		return "/UserPage/viewbooking";
	}
	
	/**
	 * Method invoked when the user clicks on the Update COVID Options button on the View Booked Flight page. Updates the booking information in 
	 * the database and returns the user to the View Booked Flight page with the updated COVID settings.
	 * 
	 * @param model The model to display the booking information in the web page
	 * @param bookingID The bookingID of the booking to be updated
	 * @param options The updated COVID options to be stored in the database
	 * @return The path to the View Booked Flight page
	 */
	@PostMapping("/submitUpdateCOVIDOptionsForm")
	public String submitUpdateCOVIDOptionsForm(Model model, @RequestParam("bookingid") long bookingID, @RequestParam("options") String[] options) {
		logic.updateCOVIDOptions(bookingID, contains(options, "mask"), contains(options, "sanitizer"), contains(options, "emptyseat"));
		
		return "redirect:/viewbooking?id=" + bookingID;
	}
	
	/**
	 * Method invoked when the user clicks on the Enable/Disable Two-Factor Authentication link in the User Settings page. Changes the 
	 * Two-Factor Authentication settings in the database for the user and redirects them to the Account Settings page with the updated 
	 * settings.
	 * 
	 * @return The path to the Account Settings page
	 */
	@RequestMapping("updateTwoFactorSettings")
	public String updateTwoFactorSettings() {
		long userID = logic.getLoggedInUserID();	
		logic.updateTwoFactorSettings(userID);
		
		return "redirect:/accountsettings";
	}
	
	/**
	 * Method invoked when the user clicks the Search Flights button. Redirects the user to a web page with a form for searching flights.
	 * 
	 * @return The path to the web page containing the Search Flights form
	 */
	@RequestMapping("/searchflights")
	public String displaySearchFlightForm() {
		return "/FlightPage/searchflights";
	}
	
	/**
	 * Method invoked when the user submits the Search Flights form. Passes the information along to the ApplicationLogic class to 
	 * perform the search.
	 * 
	 * @param source The source location provided by the user
	 * @param destination The destination location provided by the user
	 * @param date The date provided by the user
	 * @return The path to the List Flights page
	 */
	@PostMapping("/submitSearchFlightsForm")
	public String submitSearchFlightsForm(@RequestParam("source") String source, @RequestParam("destination") String destination, 
			@RequestParam("date") String date) {
		logic.searchFlights(source, destination, date);
		
		return "redirect:/listflights";	
	}
	
	/**
	 * Method called after the Flight Search Form has been handled. Obtains the search results from the ApplicationLogic class and 
	 * displays the results on a web page.
	 * 
	 * @param model The model to display the search results in the web page
	 * @return The path to hte List Flights Page
	 */
	@RequestMapping("/listflights") 
	public String listFlights(Model model) {
		long userID = logic.getLoggedInUserID();		
		List<Flight> results = logic.getSearchResults(userID);
		model.addAttribute("flights", results);
		
		return "/FlightPage/listflights";
	}
	
	/**
	 * Method invoked when a user clicks on the View Details button corresponding to a Flight on the list of 
	 * search results. Obtains the flight information from the database and redirects the user to a web page 
	 * displaying the flight information.
	 * 
	 * @param model The model to display the flight information in the web page
	 * @param index The index of the flight selected, used to locate the flight in the list of results
	 * @return The path to the View Flight page
	 */
	@GetMapping("/viewflight")
	public String displayFlight(Model model, @RequestParam("index") int index) {
		Flight flight = logic.getFlight(index);
		model.addAttribute("index", index);
		model.addAttribute("flight", flight);
		
		return "/FlightPage/viewflight";
	}
	
	/**
	 * Method invoked when the user click on the Purchase Flight button on the View Flight page. Redirects the user to a web page 
	 * containing the Purchase Flight form.
	 * 
	 * @param model The model to display the flight information in the web page
	 * @param index The index of the flight selected, used to locate the flight in the list of results
	 * @return The path to the Purchase Flight form
	 */
	@GetMapping("/purchaseflight")
	public String displayPurchaseFlightForm(Model model, @RequestParam("index") int index) {		
		Flight flight = logic.getFlight(index);
		model.addAttribute("flight", flight);
		model.addAttribute("index", index);
				
		return "/FlightPage/purchaseflight";
	}
	
	/**
	 * Method invoked when the user submits the Purchase Flight form. Passes the information along to the ApplicationLogic class to 
	 * process the purchase and redirects the user to their profile page.
	 * 
	 * @param index The index of the flight selected, used to locate the flight in the list of results
	 * @param cardNum The credit card number for the purchase
	 * @param expDate The credit card expiration date for the purchase
	 * @param secCode The credit card security code for the purchase
	 * @param options The various options selected for the purchase
	 * @return The path to the View Profile page
	 */
	@PostMapping("/submitPurchaseFlightForm")
	public String submitPurchaseFlightForm(@RequestParam("index") int index, @RequestParam("cardnum") String cardNum, 
			@RequestParam("expdate") String expDate, @RequestParam("cvc") String secCode, @RequestParam("options") String[] options) {	
		long userID = logic.getLoggedInUserID();		
		logic.bookFlight(userID, index, cardNum, expDate, secCode, contains(options, "save"), 
				contains(options, "mask"), contains(options, "sanitizer"), contains(options, "emptyseat"));
		
		return "redirect:/myprofile";
	}
	
	/**
	 * Helper method to check if array of strings contains provided string.
	 * Used for validation of check box inputs on certain form submissions.
	 * 
	 * @param elements The array of elements to be searched
	 * @param arg The argument to search for
	 * @return True if the argument is present in the array, else false
	 */
	private Boolean contains(String[] elements, String arg) {
		for (int i = 0; i < elements.length; i++)
			if (elements[i].compareTo(arg) == 0)
				return true;
		
		return false;
	}
}
