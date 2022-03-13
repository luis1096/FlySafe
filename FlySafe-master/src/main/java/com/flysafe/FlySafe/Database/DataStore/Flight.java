package com.flysafe.FlySafe.Database.DataStore;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Flight class models entries in the BookedFlight table in the database.
 */
@Entity
@Table(name = "flights")
public class Flight {
	
    /**
     * Default constructor.
     */
    public Flight() {
    }
    
    /**
     * Parameterized constructor.
     * 
     * @param airline The airline for the Flight object
     * @param dateTime the date/time for the Flight object
     * @param cost The cost for the Flight object
     * @param depCity The departing city for the Flight object
     * @param depAirportCode The departing airport code for the Flight object
     * @param arrCity The arrival city for the Flight object
     * @param arrAirportCode The arrival airport code for the Flight object
     */
	public Flight(String airline, Date dateTime, float cost, String depCity, String depAirportCode, String arrCity, String arrAirportCode) {
		this.airline = airline;
		this.dateTime = dateTime;
		this.cost = cost;
		this.depCity = depCity;
		this.depAirportCode = depAirportCode;
		this.arrCity = arrCity;
		this.arrAirportCode = arrAirportCode;
	}

    /**
     * (Primary Key) Unique flight ID to identify Flight.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private long flightID;

    /**
     * The airline through whom the flight was booked.
     */
    private String airline;

    /**
     * The price of the flight.
     */
    private float cost;

    /**
     * The date and time of the departure.
     */
    @Column(name = "date_time")
    private Date dateTime;

    /**
     * The city of the departure.
     */
    @Column(name = "dep_city")
    private String depCity;

    /**
     * The airport code for the departure.
     */
    @Column(name = "dep_code")
    private String depAirportCode;

    /**
     * The city of the arrival.
     */
    @Column(name = "arr_city")
    private String arrCity;

    /**
     * The airport code for the arrival.
     */
    @Column(name = "arr_code")
    private String arrAirportCode;
    
    /**
     * The List of BookedFlight objects corresponding to this Flight.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "flight")
    private List<BookedFlight> bookedFlights = new ArrayList<>();
    
    /**
     * Getter for flightID.
     * 
     * @return flightID
     */
	public long getFlightID() {
		return flightID;
	}

	/**
	 * Getter for airline.
	 * 
	 * @return airline
	 */
	public String getAirline() {
		return airline;
	}

	/**
	 * Getter for cost.
	 * 
	 * @return cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * Getter for dateTime.
	 * 
	 * @return dateTime
	 */
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * Getter for departure city.
	 * 
	 * @return depCity
	 */
	public String getDepCity() {
		return depCity;
	}

	/**
	 * Getter for departure airport code.
	 * 
	 * @return depAirportCode
	 */
	public String getDepAirportCode() {
		return depAirportCode;
	}

	/**
	 * Getter for arrival city.
	 * 
	 * @return arrCity
	 */
	public String getArrCity() {
		return arrCity;
	}
	
	/**
	 * Getter for arrival airport code.
	 * 
	 * @return arrAirportCode
	 */
	public String getArrAirportCode() {
		return arrAirportCode;
	}

	/**
	 * Getter for the list of BookedFlights corresponding to this Flight object.
	 * 
	 * @return bookedFlights
	 */
	public List<BookedFlight> getBookedFlights() {
		return bookedFlights;
	}
	
	/**
	 * Setter for airline.
	 * 
	 * @param airline The airline for the Flight object
	 */
	public void setAirline(String airline) {
		this.airline = airline;
	}

	/**
	 * Setter for cost.
	 * 
	 * @param cost The cost for the Flight object
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * Setter for dateTime.
	 * 
	 * @param dateTime The date/time for the Flight object
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Setter for departure city.
	 * 
	 * @param depCity The departure city for the Flight object
	 */
	public void setDepCity(String depCity) {
		this.depCity = depCity;
	}

	/**
	 * Setter for departure airport code.
	 * 
	 * @param depAirportCode The departure airport code for the Flight object
	 */
	public void setDepAirportCode(String depAirportCode) {
		this.depAirportCode = depAirportCode;
	}

	/**
	 * Setter for arrival city.
	 * 
	 * @param arrCity The arrival city for the Flight object
	 */
	public void setArrCity(String arrCity) {
		this.arrCity = arrCity;
	}
	
	/**
	 * Setter for arrival airport code.
	 * 
	 * @param arrAirportCode The arrival airport code for the Flight object
	 */
	public void setArrAirportCode(String arrAirportCode) {
		this.arrAirportCode = arrAirportCode;
	}
	
	/**
	 * Method to add a BookedFlight object to the list of BookedFlight objects associated with this Flight.
	 * 
	 * @param bookedFlight The BookedFlight object to add to the list
	 */
	public void addBookedFlight(BookedFlight bookedFlight) {
		bookedFlights.add(bookedFlight);
		bookedFlight.setFlight(this);
	}
}