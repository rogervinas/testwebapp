package com.rogervinas.testwebapp.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.rogervinas.testwebapp.model.User;

public class UserDaoMem implements UserDao
{
	private final Map<String, User> users = new HashMap<String, User>();
	
	@Override
	public void add(User user)
	{
		users.put(user.getUsername(), user);
	}

	@Override
	public void modify(User user)
	{
		users.replace(user.getUsername(), user);		
	}

	@Override
	public void remove(User user)
	{
		users.remove(user.getUsername());
	}

	@Override 
	public User get(String username) {
		return users.get(username);
	}
	
	@Override
	public Collection<User> getAll()
	{
		Collection<User> all = new ArrayList<User>();
		all.addAll(users.values());
		return all;
	}
}
