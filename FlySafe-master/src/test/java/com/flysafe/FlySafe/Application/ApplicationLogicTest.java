package com.flysafe.FlySafe.Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.flysafe.FlySafe.Database.DataFacade;
import com.flysafe.FlySafe.Database.DataStore.*;
import com.flysafe.FlySafe.Security.FlySafeUserDetails;

@SpringBootTest
public class ApplicationLogicTest {
	
	@Spy
	@InjectMocks
	private ApplicationLogic logic;
	
	@Mock
	private DataFacade dataFacade;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCreateAccount() {
		Mockito.when(dataFacade.getUser(1L)).thenReturn(DBPrototype.getUser());
		Mockito.when(dataFacade.addNewUser("Michael", "McCarthy", "mmcca083@fiu.edu", "5861234567", "password")).thenReturn(DBPrototype.getUser());
		
		logic.createAccount("Michael", "McCarthy", "mmcca083@fiu.edu", "5861234567", "password");
		User saved = logic.getUser(1L);
		assertNotNull(saved);
	}
	
	@Test
	public void testGetUser() {
		Mockito.when(dataFacade.getUser(1L)).thenReturn(DBPrototype.getUser());
		Mockito.when(dataFacade.getUser(15L)).thenReturn(null);
		
		User saved = logic.getUser(1L);
		assertNotNull(saved);
		
		User notFound = logic.getUser(15L);
		assertNull(notFound);
	}

	@Test
	public void testSearchFlights() {	
		Mockito.doReturn(1L).when(logic).getLoggedInUserID();
		List<Flight> results = logic.searchFlights("Miami", "Detroit", "2020-12");
		assertNotEquals(0, results.size());
	}
	
	@Test
	public void testGetSearchResults() {		
		Mockito.doReturn(1L).when(logic).getLoggedInUserID();
		logic.searchFlights("Miami", "Detroit", "2020-12");
		List<Flight> results = logic.getSearchResults(1L);
		assertNotEquals(0, results.size());
		
		results = logic.getSearchResults(15L);
		assertEquals(0, results.size());
	}
	
	@Test
	public void testBookFlight() throws Exception {
		Flight flight = DBPrototype.getFlight();
		Mockito.doReturn(flight).when(logic).getFlight(0);
		Mockito.when(dataFacade.getBookedFlight(1L)).thenReturn(DBPrototype.getBookedFlight());
			
		logic.bookFlight(1L, 0, "1111222233334444", "11/23", "111", false, true, true, true);
		BookedFlight booking = logic.getBookedFlight(1L);
		
		assertNotNull(booking);
	}
	
	@Test
	public void testGetFlight() throws Exception {
		Mockito.doReturn(1L).when(logic).getLoggedInUserID();
		logic.searchFlights("Miami", "Detroit", "2020-12");

		Flight flight = logic.getFlight(0);		
		assertNotNull(flight);
		
		flight = logic.getFlight(-1);
		assertNull(flight);
	}
	
	@Test
	public void testGetBookedFlight() {
		Mockito.when(dataFacade.getBookedFlight(1L)).thenReturn(DBPrototype.getBookedFlight());
		BookedFlight booking = logic.getBookedFlight(1L);
		
		assertNotNull(booking);
	}
	
	@Test
	public void testUpdateCOVIDOptions() throws Exception {
		logic.updateCOVIDOptions(1L, false, false, false);				
	}
	
	
	@Test
	public void testUpdateTwoFactorSettings() throws Exception {
		logic.updateTwoFactorSettings(1L);				
	}
}
