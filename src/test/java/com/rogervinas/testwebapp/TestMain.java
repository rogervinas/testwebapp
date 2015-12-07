package com.rogervinas.testwebapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Session;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMain
{
	private static final Logger logger = LoggerFactory.getLogger(TestMain.class);
	
	private static final int USERS_COUNT = 6;
	private static final int USER_INDEX = USERS_COUNT / 2;
	private static final String USER_ID = AppModelTest.getUserId(USER_INDEX);
	private static final String USER_PASS = AppModelTest.getUserPass(USER_INDEX);
	
	private static HelperApp helperApp;
	private static HelperHttpClient helperHttp;
	
	@BeforeClass
	public static void init() throws Exception {
		logger.info(">>> Init test prerequisites ...");
		helperApp = new HelperApp(USERS_COUNT);
		helperHttp = new HelperHttpClient();
		helperApp.start();
	}
	
	@AfterClass
	public static void shutdown() {
		logger.info(">>> Shutdown test prerequisites ...");
		helperApp.stop();
	}
	
	// Tests
	
	@Test
	public void test010PageNotFound() throws Exception {
		logger.info("*** Test 010: Page not found");
		HttpGet httpget = new HttpGet(helperApp.getUrl("/aaa/bbb/ccc"));
		HttpResponse response = helperHttp.getHttpClient().execute(httpget);
		String responseContent = helperHttp.getResponseContent(response); 
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 404);
	}
	
	@Test
	public void test020PageWithoutBeingLoggedIn() throws Exception {
		logger.info("*** Test 020: Page without being logged in");
		String path = AppModelTest.getAccessId(1);
		HttpGet httpget = new HttpGet(helperApp.getUrl(path));
		HttpResponse response = helperHttp.getHttpClient().execute(httpget);
		String responseContent = helperHttp.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 200);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, String.format("Please login to access <b>%s</b>", path));
		HelperAssert.assertContent(responseContent, "<form action=\"/login\" method=\"post\">");
	}
	
	@Test
	public void test030LoginFailUserNotFound() throws Exception {
		logger.info("*** Test 030: Login with incorrect user");
		String user = "xxxx";
		String password = "yyyy";
		testLoginFail(user, password);
	}
	
	@Test
	public void test040LoginFailWrongPassword() throws Exception {
		logger.info("*** Test 040: Login with wrong password");
		String user = USER_ID;
		String password = "yyyy";
		testLoginFail(user, password);
	}		
	
	@Test
	public void test050LoginSuccess() throws Exception {
		logger.info("*** Test 050: Login success");
		String user = USER_ID;
		String password = USER_PASS;
		String redirect = AppModelTest.getAccessId(1);
		helperHttp.getCookieStore().clear();
		HttpResponse response = testLogin(user, password, redirect);
		String responseContent = helperHttp.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 302);
		HelperAssert.assertHeader(response, "Location", redirect);
		HelperAssert.assertTrue("Cookie Session is set", helperHttp.getCookie(Session.class.getSimpleName()) != null);	
	}
	
	private void testLoginFail(String user, String password) throws Exception {
		HttpResponse response = testLogin(user, password, "/somewhere");
		String responseContent = helperHttp.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 401);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, String.format("User %s not found or incorrect password", user));
	}
	
	private HttpResponse testLogin(String user, String password, String redirect) throws Exception {
		HttpPost httppost = new HttpPost(helperApp.getUrl("/login"));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("redirect", redirect));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		return helperHttp.getHttpClient().execute(httppost);		
	}
	
	@Test
	public void test060PageAuthorized() throws Exception {
		String user = USER_ID;
		for(int i=1; i<=USER_INDEX; i++) {
			logger.info(String.format("Test 060-%d: Page authorized", i));
			String path = AppModelTest.getAccessId(i);
			HttpGet httpget = new HttpGet(helperApp.getUrl(path));
			HttpResponse response = helperHttp.getHttpClient().execute(httpget);
			String responseContent = helperHttp.getResponseContent(response);
			logger.info("\n" + responseContent);
			HelperAssert.assertStatusCode(response, 200);
			HelperAssert.assertHeader(response, "Content-Type", "text/html");
			HelperAssert.assertContent(responseContent, String.format("Page %s", path));
			HelperAssert.assertContent(responseContent, String.format("<p>Hello <b>%s</b>, you are in <b>%s</b></p>", user, path));
			HelperAssert.assertContent(responseContent, "<form action=\"/logout\" method=\"post\">");
		}
	}	
	
	@Test
	public void test070PageNotAuthorized() throws Exception {
		logger.info("*** Test 070: Page not authorized");
		String user = USER_ID;			
		String path = AppModelTest.getAccessId(USER_INDEX+1);
		HttpGet httpget = new HttpGet(helperApp.getUrl(path));
		HttpResponse response = helperHttp.getHttpClient().execute(httpget);
		String responseContent = helperHttp.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 403);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, "Page forbidden");
		HelperAssert.assertContent(responseContent, String.format("User <b>%s</b> does not have access to <b>%s</b>", user, path));
		HelperAssert.assertContent(responseContent, "<form action=\"/login\" method=\"post\">");
	}
	
	@Test
	public void test080Logout() throws Exception {
		logger.info("*** Test 080: Logout");
		String user = USER_ID;
		HttpPost httppost = new HttpPost(helperApp.getUrl("/logout"));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		httppost.setEntity(new UrlEncodedFormEntity(params));		
		HttpResponse response = helperHttp.getHttpClient().execute(httppost);
		String responseContent = helperHttp.getResponseContent(response);
		logger.info("\n" + responseContent);		
		HelperAssert.assertStatusCode(response, 200);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, "Logout Page");
		HelperAssert.assertContent(responseContent, String.format("User <b>%s</b> successfully logged out", user));
		HelperAssert.assertEquals("Cookie Session", "0", helperHttp.getCookie(Session.class.getSimpleName()).getValue());	
	}	
	
	@Test
	public void test090PageAfterLogout() throws Exception {
		logger.info("*** Test 090: Repeat Test 020");
		test020PageWithoutBeingLoggedIn();
	}
}
