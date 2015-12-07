package com.rogervinas.testwebapp;

import java.util.stream.IntStream;

import com.rogervinas.testwebapp.model.Access;
import com.rogervinas.testwebapp.model.Role;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.model.User;
import com.rogervinas.testwebapp.model.mem.AccessMem;
import com.rogervinas.testwebapp.model.mem.GenericMemDb;
import com.rogervinas.testwebapp.model.mem.RoleMem;
import com.rogervinas.testwebapp.model.mem.SessionMem;
import com.rogervinas.testwebapp.model.mem.UserMem;

public class AppModelTest implements AppModel
{
	//private static final Logger logger = LoggerFactory.getLogger(TestMemDb.class);
	
	private final GenericMemDb<User> userDb = new GenericMemDb<User>();
	private final GenericMemDb<Role> roleDb = new GenericMemDb<Role>();
	private final GenericMemDb<Access> accessDb = new GenericMemDb<Access>();
	private final GenericMemDb<Session> sessionDb = new GenericMemDb<Session>();
	
	public static String getUserId(int i) {
		return String.format("USER_%d", i);		
	}
	
	public static String getUserPass(int i) {
		return String.format("pass%d", i);		
	}	

	public static String getRoleId(int i) {
		return String.format("PAGE_%d", i);		
	}

	public static String getAccessId(int i) {
		return String.format("/page%d", i);		
	}
		
	/*
	 * Creates a test model in memory with:
	 * - <code>count</code> users named USER_1, USER_2, ...
	 * - <code>count</code> roles named PAGE_1, PAGE_2, ...
	 * - <code>count</code> pages served on /page1, /page2, ...
	 * - User USER_N has roles PAGE_1 to PAGE_N
	 * - Users with role PAGE_N can access /pageN
	 * @param num number of users/pages/roles to create
	 */	
	public AppModelTest(int count) {
		
		IntStream.range(1, count+1)
		.forEach(value -> {
			newRole(getRoleId(value)).save();
		});
		
		IntStream.range(1, count+1).forEach(value1 -> {
			User user = newUser(getUserId(value1));
			user.setPassword(getUserPass(value1));
			IntStream.range(1, value1+1).forEach(value2 -> {
				Role role = newRole(getRoleId(value2)).load();
				user.addRoles(role);
			});
			user.save();
		});

		IntStream.range(1, count+1)
		.forEach(value -> {
			Role role = newRole(getRoleId(value)).load();
			Access access = newAccess(getAccessId(value));
			access.addRoles(role);
			access.save();
		});		
	}
	
	public User newUser(String id) {
		return new UserMem(id, userDb);
	}
	
	public Role newRole(String id) {
		return new RoleMem(id, roleDb);
	}

	public Access newAccess(String id) {
		return new AccessMem(id, accessDb);
	}

	public Session newSession(String id) {
		return new SessionMem(id, sessionDb);
	}	
}
