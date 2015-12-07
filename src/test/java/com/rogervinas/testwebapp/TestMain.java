package com.rogervinas.testwebapp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMain extends TestGeneric
{
	//private static final Logger logger = LoggerFactory.getLogger(TestMain.class);
	
	private static final int USERS_COUNT = 6;
	private static final int USER_INDEX = USERS_COUNT / 2;
	private static final String USER_ID = AppModelTest.getUserId(USER_INDEX);
	private static final String USER_PASS = AppModelTest.getUserPass(USER_INDEX);

	@BeforeClass
	public static void init() throws Exception {		
		TestGeneric.init(0, USERS_COUNT, 1);
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		TestGeneric.shutdown();
	}	
	
	// Tests
	
	@Test
	public void test010PageNotFoundBeforeLogin() throws Exception {
		logHeader("Test 010: Page not found before login");
		testPageNotFound(0, "aaa/bbb/ccc");
	}
	
	@Test
	public void test020PageFoundBeforeLogin() throws Exception {
		logHeader("Test 020: Page found before login");
		testPageNotLoggedIn(0, AppModelTest.getAccessId(1));
	}
	
	@Test
	public void test030LoginFailUserNotFound() throws Exception {
		logHeader("Test 030: Login fail user not found");
		testLoginFail(0, "xxxx", "yyyy", "/zzzz");
	}
	
	@Test
	public void test040LoginFailWrongPassword() throws Exception {
		logHeader("Test 040: Login fail wrong password");
		testLoginFail(0, USER_ID, "xxxx", "/yyyy");
	}		
	
	@Test
	public void test050LoginSuccess() throws Exception {
		logHeader("Test 050: Login success");
		testLoginSuccess(0, USER_ID, USER_PASS, AppModelTest.getAccessId(1));
	}
	
	@Test
	public void test060PageAuthorized() throws Exception {
		for(int i=1; i<=USER_INDEX; i++) {
			logHeader(String.format("Test 060-%d: Page authorized", i));
			testPageAuthorized(0, USER_ID, AppModelTest.getAccessId(i));
		}
	}	
	
	@Test
	public void test070PageNotFoundAfterLogin() throws Exception {
		logHeader("Test 010: Page not found after login");
		testPageNotFound(0, "aaa/bbb/ccc");
	}	
	
	@Test
	public void test080PageNotAuthorized() throws Exception {
		logHeader("Test 080: Page not authorized");
		testPageNotAuthorized(0, USER_ID, AppModelTest.getAccessId(USER_INDEX+1));
	}
	
	@Test
	public void test090Logout() throws Exception {
		logHeader("Test 080: Logout");
		testLogoutSuccess(0, USER_ID);
	}	
	
	@Test
	public void test100PageAfterLogout() throws Exception {
		logHeader("Test 090: Page found after logout");
		testPageNotLoggedIn(0, AppModelTest.getAccessId(1));
	}
}
