package com.rogervinas.testwebapp.persistence;

import java.util.Collection;

import com.rogervinas.testwebapp.model.User;

public interface UserDao
{
	public void add(User user);
	public void modify(User user);
	public void remove(User user);
	
	public User get(String username);
	public Collection<User> getAll();	
}
