package com.rogervinas.testwebapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestGeneric
{
	private static final Logger logger = LoggerFactory.getLogger(TestGeneric.class);
	
	private static HelperApp helperApp;
	private static HelperHttpClient helperHttpClient;
	
	public static HelperApp getHelperApp() {
		return helperApp;
	}
	
	public static HelperHttpClient getHelperHttpClient() {
		return helperHttpClient;
	}
	
	public static void init(int count) throws Exception {
		logger.info(">>> Init test prerequisites ...");
		helperApp = new HelperApp(count);
		helperHttpClient = new HelperHttpClient();
		helperApp.start();
	}
	
	public static void shutdown() {
		logger.info(">>> Shutdown test prerequisites ...");
		helperApp.stop();
	}
	
	public HttpResponse testLogin(String user, String password, String redirect) throws Exception {
		HttpPost httppost = new HttpPost(helperApp.getUrl("/login"));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("redirect", redirect));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		return helperHttpClient.getHttpClient().execute(httppost);		
	}
}
