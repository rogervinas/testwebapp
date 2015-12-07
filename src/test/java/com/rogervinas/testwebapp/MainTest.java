package com.rogervinas.testwebapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.server.Server;
import com.rogervinas.testwebapp.server.ServerImpl;

public class MainTest
{
	private static final Logger logger = LoggerFactory.getLogger(MainTest.class);
	
	private BasicCookieStore cookieStore;
	private CloseableHttpClient httpclient;
	private App app;
	private String url;
	
	@Before
	public void createHttpClient() throws IOException {
		cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom()
        		.setDefaultCookieStore(cookieStore)
                .build();
        int port = Integer.parseInt(System.getProperty("port", "8081"));
        url = String.format("http://localhost:%d/", port);
        Server server = new ServerImpl(port, 5);
        AppModel model = new AppModelTest(5);
        app = new App(server, model);
        app.start();
	}
	
	@Test
	public void testPageNotFound() throws ClientProtocolException, IOException {
		logHeader("Test Page not found");
		HttpGet httpget = new HttpGet(url + "/aaa/bbb/ccc");
		CloseableHttpResponse response = httpclient.execute(httpget);
		logResponse(response);
		Assert.assertEquals(404, response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testPageWithoutBeingLoggedIn() throws ClientProtocolException, IOException {
		logHeader("Test Page without being logged in");
		HttpGet httpget = new HttpGet(url + "/page1");
		CloseableHttpResponse response = httpclient.execute(httpget);
		logResponse(response);
		Assert.assertEquals(200, response.getStatusLine().getStatusCode());		
	}
	
	/*
		for(int j=0; j<2; j++) {
	        HttpGet httpget = new HttpGet("http://localhost:3000/page1");
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
	}*/
	
	@After
	public void end() {
		app.stop();
	}
	
	private void logHeader(String name) {
		logger.info("Test\n\n" + name + "\n\n");
	}
	
	private void logResponse(HttpResponse response) throws IOException {
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
		logger.info("Response\n\n" + buffer + "\n");
	}
}
