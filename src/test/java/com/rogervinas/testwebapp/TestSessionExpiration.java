package com.rogervinas.testwebapp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSessionExpiration extends TestGeneric
{
	private static final Logger logger = LoggerFactory.getLogger(TestSessionExpiration.class);
	
	private static final int USERS_COUNT = 2;
	private static final int USER_INDEX = USERS_COUNT;
	private static final String USER_ID = AppModelTest.getUserId(USER_INDEX);
	private static final String USER_PASS = AppModelTest.getUserPass(USER_INDEX);
	private static final String USER_PAGE = AppModelTest.getAccessId(USER_INDEX);
	
	private static final int SESSION_MAX_AGE = 5;
	
	@BeforeClass
	public static void init() throws Exception {
		// Set session max age to 5 seconds
		System.setProperty("sessionMaxAge", "" + SESSION_MAX_AGE);		
		TestGeneric.init(2, USERS_COUNT, 1);
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		TestGeneric.shutdown();
	}
		
	// Tests
	
	@Test
	public void test010SessionExpires() throws Exception {
		logHeader("Test 010: Session expires");		
		// Login
		testLogin(0, USER_ID, USER_PASS, USER_PAGE);
		// Get page
		testPageAuthorized(0, USER_ID, USER_PAGE);
		// Wait to ensure session has expired
		int delay = (SESSION_MAX_AGE+1) * 1000;
		logger.info(String.format("Wait %d ms", delay));
		Thread.sleep(delay);
		// Get page again
		testPageNotLoggedIn(0, USER_PAGE);
	}
}
