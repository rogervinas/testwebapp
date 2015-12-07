package com.rogervinas.testwebapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Session;

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
		TestGeneric.init(USERS_COUNT);
	}
	
	@AfterClass
	public static void shutdown() {
		TestGeneric.shutdown();
	}
	
	// Tests
	
	@Test
	public void test010SessionExpires() throws Exception {
		logger.info("*** Test 010: Session expires");
		// Login
		getHelperHttpClient().getCookieStore().clear();
		HttpResponse response1 = testLogin(USER_ID, USER_PASS, USER_PAGE);
		String responseContent1 = getHelperHttpClient().getResponseContent(response1); 
		logger.info("\n" + responseContent1);
		HelperAssert.assertStatusCode(response1, 302);
		HelperAssert.assertTrue("Cookie Session is set", getHelperHttpClient().getCookie(Session.class.getSimpleName()) != null);
		// Get page
		HttpGet httpget = new HttpGet(getHelperApp().getUrl(USER_PAGE));
		HttpResponse response2 = getHelperHttpClient().getHttpClient().execute(httpget);
		String responseContent2 = getHelperHttpClient().getResponseContent(response2); 
		logger.info("\n" + responseContent2);
		HelperAssert.assertStatusCode(response2, 200);
		HelperAssert.assertContent(responseContent2, String.format("Page %s", USER_PAGE));
		// Wait to ensure session has expired
		int delay = (SESSION_MAX_AGE+1) * 1000;
		logger.info(String.format("Wait %d ms", delay));
		Thread.sleep(delay);
		// Get page again
		HttpResponse response3 = getHelperHttpClient().getHttpClient().execute(httpget);
		String responseContent3 = getHelperHttpClient().getResponseContent(response3); 
		logger.info("\n" + responseContent3);
		HelperAssert.assertStatusCode(response3, 200);
		HelperAssert.assertEquals("Cookie Session", "0", getHelperHttpClient().getCookie(Session.class.getSimpleName()).getValue());
		HelperAssert.assertContent(responseContent3, "Login Page");
	}
}
