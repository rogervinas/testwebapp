package com.rogervinas.testwebapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.Server;
import com.rogervinas.testwebapp.server.ServerImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest
{
	private static final Logger logger = LoggerFactory.getLogger(MainTest.class);
	
	private static final int USERS_COUNT = 6;
	private static final int USER_INDEX = USERS_COUNT / 2;
	private static final String USER_ID = AppModelTest.getUserId(USER_INDEX);
	private static final String USER_PASS = AppModelTest.getUserPass(USER_INDEX);
	
	private static BasicCookieStore cookieStore;
	private static CloseableHttpClient httpclient;
	private static App app;
	private static String url;
		
	@BeforeClass
	public static void createHttpClient() throws Exception {
		log("Init test prerequisites ...");
		cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom()
        		.setDefaultCookieStore(cookieStore)
                .build();
        int port = Integer.parseInt(System.getProperty("port", "8081"));
        url = String.format("http://localhost:%d/", port);
        Server server = new ServerImpl(port, 5);
        AppModel model = new AppModelTest(USERS_COUNT);
        app = new App(server, model);
        app.start();
	}
	
	@AfterClass
	public static void end() {
		log("Shutdown test prerequisites ...");
		app.stop();
	}
	
	// Tests
	
	@Test
	public void test010PageNotFound() throws Exception {
		log("Test 010: Page not found");
		HttpGet httpget = new HttpGet(url + "/aaa/bbb/ccc");
		CloseableHttpResponse response = httpclient.execute(httpget);
		log(response);
		assertEquals("Status code", 404, response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void test020PageWithoutBeingLoggedIn() throws Exception {
		log("Test 020: Page without being logged in");
		String path = AppModelTest.getAccessId(1);
		HttpGet httpget = new HttpGet(url + path);
		CloseableHttpResponse response = httpclient.execute(httpget);
		String responseContent = getResponseContent(response);
		log(responseContent);
		assertStatusCode(response, 200);
		assertHeader(response, "Content-Type", "text/html");
		assertContent(responseContent, String.format("Please login to access <b>%s</b>", path));
		assertContent(responseContent, "<form action=\"/login\" method=\"post\">");
	}
	
	@Test
	public void test030LoginFailUserNotFound() throws Exception {
		log("Test 030: Login with incorrect user");
		String user = "xxxx";
		String password = "yyyy";
		testLoginFail(user, password);
	}
	
	@Test
	public void test040LoginFailWrongPassword() throws Exception {
		log("Test 040: Login with wrong password");
		String user = USER_ID;
		String password = "yyyy";
		testLoginFail(user, password);
	}		
	
	@Test
	public void test050LoginSuccess() throws Exception {
		log("Test 050: Login success");
		String user = USER_ID;
		String password = USER_PASS;
		String redirect = AppModelTest.getAccessId(1);
		cookieStore.clear();
		CloseableHttpResponse response = testLogin(user, password, redirect);
		String responseContent = getResponseContent(response);
		log(responseContent);
		assertStatusCode(response, 302);
		assertHeader(response, "Location", redirect);
		assertTrue("Cookie Session is set", getCookie(Session.class.getSimpleName()) != null);	
	}
	
	@Test
	public void test060PageAuthorized() throws Exception {
		String user = USER_ID;
		for(int i=1; i<=USER_INDEX; i++) {
			log(String.format("Test 060-%d: Page authorized", i));
			String path = AppModelTest.getAccessId(i);
			HttpGet httpget = new HttpGet(url + path);
			CloseableHttpResponse response = httpclient.execute(httpget);
			String responseContent = getResponseContent(response);
			log(responseContent);
			assertStatusCode(response, 200);
			assertHeader(response, "Content-Type", "text/html");
			assertContent(responseContent, String.format("Page %s", path));
			assertContent(responseContent, String.format("<p>Hello <b>%s</b>, you are in <b>%s</b></p>", user, path));
			assertContent(responseContent, "<form action=\"/logout\" method=\"post\">");
		}
	}	
	
	@Test
	public void test070PageNotAuthorized() throws Exception {
		log("Test 070: Page not authorized");
		String user = USER_ID;			
		String path = AppModelTest.getAccessId(USER_INDEX+1);
		HttpGet httpget = new HttpGet(url + path);
		CloseableHttpResponse response = httpclient.execute(httpget);
		String responseContent = getResponseContent(response);
		log(responseContent);
		assertStatusCode(response, 403);
		assertHeader(response, "Content-Type", "text/html");
		assertContent(responseContent, "Page forbidden");
		assertContent(responseContent, String.format("User <b>%s</b> does not have access to <b>%s</b>", user, path));
		assertContent(responseContent, "<form action=\"/login\" method=\"post\">");
	}
	
	@Test
	public void test080Logout() throws Exception {
		log("Test 080: Logout");
		String user = USER_ID;
		HttpPost httppost = new HttpPost(url + "/logout");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		httppost.setEntity(new UrlEncodedFormEntity(params));		
		CloseableHttpResponse response = httpclient.execute(httppost);
		String responseContent = getResponseContent(response);
		log(responseContent);		
		assertStatusCode(response, 200);
		assertHeader(response, "Content-Type", "text/html");
		assertContent(responseContent, "Logout Page");
		assertContent(responseContent, String.format("User <b>%s</b> successfully logged out", user));
		assertEquals("Cookie Session", "0", getCookie(Session.class.getSimpleName()).getValue());	
	}	
	
	@Test
	public void test090PageAfterLogout() throws Exception {
		log("Test 090: Repeat Test 020");
		test020PageWithoutBeingLoggedIn();
	}
	
	// Utils
	
	private void testLoginFail(String user, String password) throws Exception {
		CloseableHttpResponse response = testLogin(user, password, "/somewhere");
		String responseContent = getResponseContent(response);
		log(responseContent);
		assertStatusCode(response, 401);
		assertHeader(response, "Content-Type", "text/html");
		assertContent(responseContent, String.format("User %s not found or incorrect password", user));
	}
	
	private CloseableHttpResponse testLogin(String user, String password, String redirect) throws Exception {
		HttpPost httppost = new HttpPost(url + "/login");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("redirect", redirect));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		return httpclient.execute(httppost);		
	}

	private void assertTrue(String message, boolean condition) {
		Assert.assertTrue(message, condition);
		logger.info(">>> " + message );
	}
	
	private void assertEquals(String message, Object expected, Object actual) {
		Assert.assertEquals(message, expected, actual);
		logger.info(">>> " + message + " is " + expected);			
	}
	
	private void assertContent(String content, String expectedText) {
		assertTrue(String.format("Content contains '%s'", expectedText), content.contains(expectedText));		
	}
	
	private void assertHeader(HttpResponse response, String headerName, String expectedValue) {
		Header[] headers = response.getHeaders(headerName);
		String actualValue = headers.length > 0 ? headers[0].getValue() : null;
		assertEquals(headerName, expectedValue, actualValue);		
	}
	
	private void assertStatusCode(HttpResponse response, int code) {
		assertEquals("Status code", code, response.getStatusLine().getStatusCode());
	}
	
	private Cookie getCookie(String name) {
		for(Cookie cookie : cookieStore.getCookies()) {
			if(cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}
	
	private String getResponseContent(HttpResponse response) throws IOException {
		StringBuffer buffer = new StringBuffer();		
		HttpEntity entity = response.getEntity();
		buffer.append(response.getStatusLine() + "\n");
		for(Header header : response.getAllHeaders()) {
			buffer.append(header.toString() + "\n");
		}
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
			String line;
			do {
				line = reader.readLine();
				if(line!=null) {
					buffer.append(line + "\n");
				}
			} while(line != null);
		}	
		return buffer.toString().trim();
	}
	
	private static final String logSep = "************************************************";
	
	private static void log(String text) {
		logger.info("\n\n" + logSep + "\n" + text + "\n" + logSep + "\n");
	}
	
	private void log(HttpResponse response) throws IOException {
		log(getResponseContent(response));
	}
}
