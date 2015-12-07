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
	
	/*
	 * Creates a test model in memory with:
	 * - <code>num</code> users named USER_1, USER_2, ...
	 * - <code>num</code> roles named PAGE_1, PAGE_2, ...
	 * - <code>num</code> pages served on /page1, /page2, ...
	 * - User USER_N has roles PAGE_1 to PAGE_N
	 * - Users with role PAGE_N can access /pageN
	 * @param num number of users/pages/roles to create
	 */	
	public AppModelTest(int num) {
				
		String userFormat = "USER_%d";		
		String roleFormat = "PAGE_%d";
		String pageFormat = "/page%d";
		
		IntStream.range(1, num)
		.forEach(value -> {
			newRole(String.format(roleFormat,value)).save();
		});
		
		IntStream.range(1, num).forEach(value1 -> {
			User user = newUser(String.format(userFormat,value1));
			user.setPassword(String.format("pass%d", value1));
			IntStream.range(1, value1+1).forEach(value2 -> {
				Role role = newRole(String.format(roleFormat,value2)).load();
				user.addRoles(role);
			});
			user.save();
		});

		IntStream.range(1, num)
		.forEach(value -> {
			Role role = newRole(String.format(roleFormat,value)).load();
			Access access = newAccess(String.format(pageFormat,value));
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
