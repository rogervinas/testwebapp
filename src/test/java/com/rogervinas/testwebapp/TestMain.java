package com.rogervinas.testwebapp;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMain
{
	private BasicCookieStore cookieStore;
	private CloseableHttpClient httpclient;
	Main main;
	
	@Before
	public void createHttpClient() throws IOException {
		cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom()
        		.setDefaultCookieStore(cookieStore)
                .build();	
        main = new Main();
        main.start();
	}
	
	@Test
	public void testSomething() throws ClientProtocolException, IOException {
		for(int j=0; j<2; j++) {
	        HttpGet httpget = new HttpGet("http://localhost:3000/");
	        CloseableHttpResponse response1 = httpclient.execute(httpget);
	        try {
	            HttpEntity entity = response1.getEntity();
	
	            System.out.println("RESPONSE: " + response1.getStatusLine());
	            EntityUtils.consume(entity);
	
	            System.out.println("COOKIES:");
	            List<Cookie> cookies = cookieStore.getCookies();
	            if (cookies.isEmpty()) {
	                System.out.println("None");
	            } else {
	                for (int i = 0; i < cookies.size(); i++) {
	                    System.out.println("- " + cookies.get(i).toString());
	                }
	            }
	            System.out.println();
	        } finally {
	            response1.close();
	        }
		}
	}
	
	@After
	public void end() {
		main.stop();
	}
}
