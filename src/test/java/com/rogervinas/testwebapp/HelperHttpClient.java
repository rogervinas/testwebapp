package com.rogervinas.testwebapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HelperHttpClient
{
	protected BasicCookieStore cookieStore;
	protected CloseableHttpClient httpClient;
	
	public HelperHttpClient() {
		this.cookieStore = new BasicCookieStore();
        this.httpClient = HttpClients.custom()
        		.setDefaultCookieStore(cookieStore)
                .build();		
	}
	
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	public CookieStore getCookieStore() {
		return cookieStore;
	}	
	
	public Cookie getCookie(String name) {
		for(Cookie cookie : cookieStore.getCookies()) {
			if(cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}	
	
	public String getResponseContent(HttpResponse response) throws IOException {
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
}
