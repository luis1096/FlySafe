package com.flysafe.FlySafe.Presentation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.flysafe.FlySafe.Application.ApplicationLogic;
import com.flysafe.FlySafe.Database.DataStore.DBPrototype;
import com.flysafe.FlySafe.Database.DataStore.Flight;

@SpringBootTest
public class PresentationTest {
	
	@InjectMocks
	private Presentation presentation;
	
	@Mock
	private ApplicationLogic logic;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDisplayRegUserHomePage() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		Mockito.when(logic.getUser(1L)).thenReturn(DBPrototype.getUser());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayRegUserHomePage(model);
		assertEquals("/UserPage/home", result);
	}
	
	@Test
	public void testDisplayLoginForm() {
		String result = presentation.displayLoginForm();
		assertEquals("/UserPage/login", result);
	}
	
	@Test
	public void testSubmitLoginForm() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		Mockito.when(logic.getUser(1L)).thenReturn(DBPrototype.getUser());
		Model model = Mockito.mock(Model.class);
		String result = presentation.submitLoginForm(model);
		assertEquals("redirect:/home", result);
	}
	
	@Test
	public void testDisplayCreateAccountForm() {
		String result = presentation.displayCreateAccountForm();
		assertEquals("/UserPage/createaccount", result);
	}
	
	@Test
	public void testSubmitCreateAccountForm() {
		String result = presentation.submitCreateAccountForm("Michael", "McCarthy", "mmccarthy731@gmail.com", "5864847712", "password");
		assertEquals("redirect:/home", result);
	}
	
	@Test
	public void testDisplaySearchFlightsForm() {
		String result = presentation.displaySearchFlightForm();
		assertEquals("/FlightPage/searchflights", result);
	}
	
	@Test
	public void testDisplayTwoFactorAuthForm() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		Mockito.when(logic.getUser(1L)).thenReturn(DBPrototype.getUser());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayTwoFactorAuthForm(model);
		assertEquals("/UserPage/authenticate", result);
	}
	
	@Test
	public void testSubmitTwoFactorAuthForm() {
		Mockito.when(logic.validateTwoFactorAuth("123456", "123456")).thenReturn(true);
		Model model = Mockito.mock(Model.class);
		String result = presentation.submitTwoFactorAuthForm(model, "123456", "123456");
		assertEquals("redirect:/home", result);
	}
	
	@Test
	public void testDisplayErrorPage() {
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayAuthErrorPage(model, true);
		assertEquals("/UserPage/autherror", result);
	}
	
	@Test
	public void testDisplayProfilePage() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		Mockito.when(logic.getUser(1L)).thenReturn(DBPrototype.getUser());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayProfilePage(model);
		assertEquals("/UserPage/myprofile", result);
	}
	
	@Test
	public void testDisplayAccountSettingsPage() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		Mockito.when(logic.getUser(1L)).thenReturn(DBPrototype.getUser());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayAccountSettingsPage(model);
		assertEquals("/UserPage/accountsettings", result);
	}
	
	@Test
	public void testDisplayCOVIDOptionsPage() {
		Mockito.when(logic.getBookedFlight(1L)).thenReturn(DBPrototype.getBookedFlight());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayCOVIDOptionsPage(model, 1L);
		assertEquals("/UserPage/viewbooking", result);
	}
	
	@Test
	public void testSubmitUpdateCOVIDOptionsForm() {
		Model model = Mockito.mock(Model.class);
		String[] options = { "mask", "sanitizer" };
		String result = presentation.submitUpdateCOVIDOptionsForm(model, 1L, options);
		assertEquals("redirect:/viewbooking?id=1", result);
	}
	
	@Test
	public void testUpdateTwoFactorSettings() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		String result = presentation.updateTwoFactorSettings();
		assertEquals("redirect:/accountsettings", result);
	}
	
	@Test
	public void submitSearchFlightsForm() {
		String result = presentation.submitSearchFlightsForm("Miami", "Detroit", "2020-12");
		assertEquals("redirect:/listflights", result);
	}
	
	@Test
	public void testListFlights() throws Exception {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		Mockito.when(logic.getSearchResults(1L)).thenReturn(new ArrayList<Flight>());
		Model model = Mockito.mock(Model.class);
		String result = presentation.listFlights(model);
		assertEquals("/FlightPage/listflights", result);
	}
	
	@Test
	public void testDisplayFlight() throws Exception {
		Mockito.when(logic.getFlight(0)).thenReturn(DBPrototype.getFlight());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayFlight(model, 0);
		assertEquals("/FlightPage/viewflight", result);
	}
	
	@Test
	public void testPurchaseFlight() throws Exception {
		Mockito.when(logic.getFlight(0)).thenReturn(DBPrototype.getFlight());
		Model model = Mockito.mock(Model.class);
		String result = presentation.displayPurchaseFlightForm(model, 0);
		assertEquals("/FlightPage/purchaseflight", result);
	}
	
	@Test
	public void testSubmitPurchaseFlightForm() {
		Mockito.when(logic.getLoggedInUserID()).thenReturn(1L);
		String[] options = { "mask", "sanitizer" };
		String result = presentation.submitPurchaseFlightForm(0, "1111222233334444", "11/23", "111", options);
		assertEquals("redirect:/myprofile", result);
	}
}
