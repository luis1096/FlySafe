package com.flysafe.FlySafe.Database.DataStore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBPrototype {

	public static User getUser() {
		return new User("Michael", "McCarthy", "5861234567", "mmcca083@fiu.edu", "password");
	}
	
	public static Flight getFlight() throws Exception {
		Date date = new SimpleDateFormat("MM/dd/yyyy").parse("12/23/2020");
		return new Flight("Delta", date, (float)100.00, "Miami", "MIA", "Detroit", "DTW");
	}
	
	public static BookedFlight getBookedFlight() {
		return new BookedFlight(100, true, true, true);
	}
	
	public static PaymentInfo getPaymentInfo() {
		return new PaymentInfo("1111222233334444", "11/23");
	}
	
	public static Role getRole() {
		return new Role("TESTER");
	}
}
