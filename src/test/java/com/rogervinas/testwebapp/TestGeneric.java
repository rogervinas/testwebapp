package com.rogervinas.testwebapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Session;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestGeneric
{
	private static final Logger logger = LoggerFactory.getLogger(TestGeneric.class);

	private static HelperApp helperApp;
	private static HelperHttpClients helperHttpClients;
		
	public static HelperApp getHelperApp() {
		return helperApp;
	}
	
	public static HelperHttpClients getHelperHttpClients() {
		return helperHttpClients;
	}	
	
	public static void init(int portOffset, int usersCount, int httpClientsCount) throws Exception {
		logHeader("Init test");
		int port = portOffset + Integer.parseInt(System.getProperty("port", "8081"));
		helperApp = new HelperApp(port, usersCount);
		helperApp.start();
		helperHttpClients = new HelperHttpClients(httpClientsCount);
	}
	
	public static void shutdown() throws Exception {
		logHeader("Shutdown test");
		helperApp.stop();
		helperHttpClients.close();
	}	
	
	// Tests
	
	public HttpResponse testLogin(int client, String user, String password, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpPost httppost = new HttpPost(getHelperApp().getUrl("/login"));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("redirect", page));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		return helperHttpClient.getHttpClient().execute(httppost);		
	}
	
	public void testLoginSuccess(int client, String user, String pass, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpResponse response = testLogin(client, user, pass, page);
		String responseContent = helperHttpClient.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 302);
		HelperAssert.assertHeader(response, "Location", page);
		HelperAssert.assertTrue("Cookie Session is set", helperHttpClient.getCookie(Session.class.getSimpleName()) != null);			
	}
	
	public void testLoginFail(int client, String user, String password, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpResponse response = testLogin(client, user, password, page);
		String responseContent = helperHttpClient.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 401);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, "Login Page");
		HelperAssert.assertContent(responseContent, String.format("User %s not found or incorrect password", user));
	}	
	
	public void testLogoutSuccess(int client, String user) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpPost httppost = new HttpPost(getHelperApp().getUrl("/logout"));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		httppost.setEntity(new UrlEncodedFormEntity(params));		
		HttpResponse response = helperHttpClient.getHttpClient().execute(httppost);
		String responseContent = helperHttpClient.getResponseContent(response);
		logger.info("\n" + responseContent);		
		HelperAssert.assertStatusCode(response, 200);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, "Logout Page");
		HelperAssert.assertContent(responseContent, String.format("User <b>%s</b> successfully logged out", user));
		HelperAssert.assertEquals("Cookie Session", "0", helperHttpClient.getCookie(Session.class.getSimpleName()).getValue());			
	}
	
	public void testPageNotFound(int client, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpGet httpget = new HttpGet(getHelperApp().getUrl(page));
		HttpResponse response = helperHttpClient.getHttpClient().execute(httpget);
		String responseContent = helperHttpClient.getResponseContent(response); 
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 404);
		HelperAssert.assertContent(responseContent, "Page Not Found");
	}
	
	public void testPageNotLoggedIn(int client, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpGet httpget = new HttpGet(getHelperApp().getUrl(page));
		HttpResponse response = helperHttpClient.getHttpClient().execute(httpget);
		String responseContent = helperHttpClient.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 200);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, "Login Page");
		HelperAssert.assertContent(responseContent, String.format("Please login to access <b>%s</b>", page));
		HelperAssert.assertContent(responseContent, "<form action=\"/login\" method=\"post\">");	
	}
	
	public void testPageAuthorized(int client, String user, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpGet httpget = new HttpGet(getHelperApp().getUrl(page));
		HttpResponse response = helperHttpClient.getHttpClient().execute(httpget);
		String responseContent = helperHttpClient.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 200);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, String.format("Page %s", page));
		HelperAssert.assertContent(responseContent, String.format("<p>Hello <b>%s</b>, you are in <b>%s</b></p>", user, page));
		HelperAssert.assertContent(responseContent, "<form action=\"/logout\" method=\"post\">");		
	}
	
	public void testPageNotAuthorized(int client, String user, String page) throws Exception {
		HelperHttpClient helperHttpClient = getHelperHttpClients().getHelperHttpClient(client);
		HttpGet httpget = new HttpGet(getHelperApp().getUrl(page));
		HttpResponse response = helperHttpClient.getHttpClient().execute(httpget);
		String responseContent = helperHttpClient.getResponseContent(response);
		logger.info("\n" + responseContent);
		HelperAssert.assertStatusCode(response, 403);
		HelperAssert.assertHeader(response, "Content-Type", "text/html");
		HelperAssert.assertContent(responseContent, "Page Forbidden");
		HelperAssert.assertContent(responseContent, String.format("User <b>%s</b> does not have access to <b>%s</b>", user, page));
		HelperAssert.assertContent(responseContent, "<form action=\"/login\" method=\"post\">");		
	}
	
	public static void logHeader(String name) {
		String header = name + " ";
		while(header.length() < 100) {
			header += "-";
		}
		logger.info(name);
	}
}
