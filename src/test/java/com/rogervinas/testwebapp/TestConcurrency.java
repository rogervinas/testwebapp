package com.rogervinas.testwebapp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConcurrency extends TestGeneric
{
	//private static final Logger logger = LoggerFactory.getLogger(TestConcurrency.class);
	
	private static final int USERS_COUNT = 10;
	private static final int HTTP_CLIENTS_COUNT = USERS_COUNT * 2;
	
	@BeforeClass
	public static void init() throws Exception {	
		TestGeneric.init(1, USERS_COUNT, HTTP_CLIENTS_COUNT);
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		TestGeneric.shutdown();
	}
	
	// Tests
	
	@Test
	public void test010Concurrency() throws Exception {
		int count = HTTP_CLIENTS_COUNT;
		logHeader(String.format("Test 010: Concurrency x %d", count));
		// Execute
		ExecutorService executor = new ScheduledThreadPoolExecutor(count);
		@SuppressWarnings("unchecked")
		Future<Throwable>[] futures = (Future<Throwable>[]) new Future<?>[count];		
		for(int i=0; i < futures.length; i++) {
			futures[i] = executor.submit(new ClientTest(i));
		}
		// Get results
		for(int i=0; i < futures.length; i++) {
			Throwable e = futures[i].get();
			HelperAssert.assertTrue(
					String.format("Client Test %d/%d %s", 
							i+1,
							count,
							e == null ? "OK" : "ERROR " + e.getMessage()
					),
					e == null
			);			
		}
	}
	
	private class ClientTest implements Callable<Throwable> {

		private final int client_i;
		
		public ClientTest(int client_i) {
			this.client_i = client_i;
		}
		
		@Override
		public Throwable call() throws Exception
		{
			try {
				int user_i = client_i % USERS_COUNT + 1;
				String user = AppModelTest.getUserId(user_i);
				String pass = AppModelTest.getUserPass(user_i);
				String page = AppModelTest.getAccessId(user_i);
				// Login
				testLoginSuccess(client_i, user, pass, page);
				// Get pages 1 to user_i
				for(int page_i=1; page_i<=user_i; page_i++) {
					testPageAuthorized(client_i, user, AppModelTest.getAccessId(page_i));
				}
				// Get page user_i + 1 (except for last user)
				if(user_i < USERS_COUNT) {
					testPageNotAuthorized(client_i, user, AppModelTest.getAccessId(user_i + 1));
				}
				// Get page not found
				testPageNotFound(client_i, "/xxxx");				
				// Logout
				testLogoutSuccess(client_i, user);
			} catch(AssertionError e) {
				return e;
			}
			return null;
		}
	}
}
