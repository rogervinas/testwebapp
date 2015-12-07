package com.rogervinas.testwebapp;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperAssert
{
	private static final Logger logger = LoggerFactory.getLogger(HelperAssert.class);
	
	public static void assertTrue(String message, boolean condition) {
		Assert.assertTrue(message, condition);
		logger.info(">>> " + message );
	}
	
	public static void assertEquals(String message, Object expected, Object actual) {
		Assert.assertEquals(message, expected, actual);
		logger.info(">>> " + message + " is " + expected);			
	}
	
	public static void assertContent(String content, String expectedText) {
		assertTrue(String.format("Content contains '%s'", expectedText), content.contains(expectedText));		
	}

	public static void assertStatusCode(HttpResponse response, int expectedCode) {
		assertEquals("Status code", expectedCode, response.getStatusLine().getStatusCode());
	}
	
	public static void assertHeader(HttpResponse response, String headerName, String expectedValue) {
		Header[] headers = response.getHeaders(headerName);
		String actualValue = headers.length > 0 ? headers[0].getValue() : null;
		assertEquals(headerName, expectedValue, actualValue);		
	}
}
