package com.flysafe.FlySafe;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.flysafe.FlySafe.Database.DataStore.*;
import com.flysafe.FlySafe.Database.DataFacadeTest;
import com.flysafe.FlySafe.Application.ApplicationLogicTest;
import com.flysafe.FlySafe.Presentation.PresentationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
	BookedFlightRepositoryTest.class,
	FlightRepositoryTest.class,
	PaymentInfoRepositoryTest.class,
	RoleRepositoryTest.class,
	UserRepositoryTest.class,
	UserTest.class,
	DataFacadeTest.class,
	ApplicationLogicTest.class,
	PresentationTest.class
})

class FlySafeApplicationTests {

}
