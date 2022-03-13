package com.flysafe.FlySafe.Database;
import com.flysafe.FlySafe.Database.DataStore.*;
import org.junit.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.*;


@SpringBootTest
public class DataFacadeTest {
	private DataFacade dataFacade;
	
	@Before
	public void setUp() throws Exception {
		dataFacade = new DataFacade();
	}
	
	@After
	public void tearDown() throws Exception {
		dataFacade = null;
	}
    
    @Test
    public void testAddNewUser() {
    	User user = DBPrototype.getUser();
    	User saved = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNum(), user.getPassword());
    	assertNotNull(saved);
    }
    
    @Test
    public void testAddBookedFlight() throws Exception {
    	User user = DBPrototype.getUser();
    	Flight flight = DBPrototype.getFlight();
    	PaymentInfo card = DBPrototype.getPaymentInfo();
    	
    	user = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getPhoneNum(), user.getEmail(), user.getPassword());
    	
    	dataFacade.addBookedFlight(user.getUserID(), flight, card.getCardNum(), card.getExpDate(), false, true, true, true, 100);
    	user = dataFacade.getUser(user.getUserID());
    	
    	assertEquals(1, user.getBookedFlights().size());
    	assertEquals(0, user.getCards().size());
    	assertEquals(100, user.getRewardPoints());
    	
    	dataFacade.addBookedFlight(user.getUserID(), flight, card.getCardNum(), card.getExpDate(), true, true, true, true, 200);
    	user = dataFacade.getUser(user.getUserID());

    	assertEquals(2, user.getBookedFlights().size());
    	assertEquals(1, user.getCards().size());
    	assertEquals(300, user.getRewardPoints());   	
    }
    
    @Test
    public void testGetFlight() throws Exception {
    	User user = DBPrototype.getUser();
    	Flight flight = DBPrototype.getFlight();
    	PaymentInfo card = DBPrototype.getPaymentInfo();
    	
    	user = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getPhoneNum(), user.getEmail(), user.getPassword());
    	
    	dataFacade.addBookedFlight(user.getUserID(), flight, card.getCardNum(), card.getExpDate(), false, true, true, true, 100);
    	flight = dataFacade.getFlight(1L);
    	assertNotNull(flight);
    	
    	flight = dataFacade.getFlight(15L);
    	assertNull(flight);
    }
    
    @Test
    public void testGetBookedFlight() throws Exception {
    	User user = DBPrototype.getUser();
    	Flight flight = DBPrototype.getFlight();
    	PaymentInfo card = DBPrototype.getPaymentInfo();
    	
    	user = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getPhoneNum(), user.getEmail(), user.getPassword());
    	
    	dataFacade.addBookedFlight(user.getUserID(), flight, card.getCardNum(), card.getExpDate(), false, true, true, true, 100);
    	BookedFlight booking = dataFacade.getBookedFlight(1L);
    	assertNotNull(booking);
    	
    	booking = dataFacade.getBookedFlight(15L);
    	assertNull(booking);
    }
    
    @Test
    public void testGetUser() {
    	User user = DBPrototype.getUser();
    	User saved = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getPhoneNum(), user.getEmail(), user.getPassword());
    	
    	user = dataFacade.getUser(saved.getUserID());
    	assertNotNull(user);
    	
    	user = dataFacade.getUser(15L);
        assertNull(user);
    }
    
    @Test
    public void testUpdateTwoFactorSettings() {
    	User user = DBPrototype.getUser();
    	User saved = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getPhoneNum(), user.getEmail(), user.getPassword());
    	
    	assertEquals(false, saved.getTwoFactorEnabled());
    	
    	dataFacade.updateTwoFactorSettings(saved.getUserID());
    	User updated = dataFacade.getUser(saved.getUserID());
    	
    	assertEquals(true, updated.getTwoFactorEnabled());
    }
    
    @Test
    public void testUpdateCOVIDSettings() throws Exception {
    	User user = DBPrototype.getUser();
    	Flight flight = DBPrototype.getFlight();
    	PaymentInfo card = DBPrototype.getPaymentInfo();
    	
    	user = dataFacade.addNewUser(user.getFirstName(), user.getLastName(), user.getPhoneNum(), user.getEmail(), user.getPassword());
    	
    	dataFacade.addBookedFlight(user.getUserID(), flight, card.getCardNum(), card.getExpDate(), false, true, true, true, 100);
    	BookedFlight booking = dataFacade.getBookedFlight(1L);
    	
    	assertEquals(true, booking.getMaskRequested());
    	assertEquals(true, booking.getSanitizerRequested());
    	assertEquals(true, booking.getEmptyAdjSeatRequested());
    	
    	dataFacade.updateCOVIDSettings(booking.getBookingID(), false, false, false);
    	booking = dataFacade.getBookedFlight(1L);
    	
    	assertEquals(false, booking.getMaskRequested());
    	assertEquals(false, booking.getSanitizerRequested());
    	assertEquals(false, booking.getEmptyAdjSeatRequested());
    }
}
