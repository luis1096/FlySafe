package com.flysafe.FlySafe.Database.DataStore;
import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
	private User user;
	
	@Before
	public void setUp() throws Exception {
		user = new User("Michael", "McCarthy", "5864847712", "mmcca083@fiu.edu", "password");
	}
	
	@After
	public void tearDown() throws Exception {
		user = null;
	}
	
	@Test
	public void testUser() {
		assertEquals("twoFactorEnabled", false, user.getTwoFactorEnabled());
		assertEquals("rewardPoints", 0, user.getRewardPoints());
	}
	
	@Test
	public void testUpdateTwoFactorSettings() {
		user.updateTwoFactorSettings();
		assertEquals("twoFactorEnabled", true, user.getTwoFactorEnabled());
	}
	
	@Test
	public void testAddPoints() {
		user.addPoints(100);
		assertEquals("rewardPoints", 100, user.getRewardPoints());
	}
	
	@Test
	public void testRemovePoints() {
		user.removePoints(50);
		assertEquals("rewardPoints", -50, user.getRewardPoints());
	}
}
