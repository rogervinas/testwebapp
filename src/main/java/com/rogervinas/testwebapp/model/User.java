package com.rogervinas.testwebapp.model;

import java.util.Arrays;

public abstract class User extends ActiveRecord<User>
{
	private String password;
	
	public User(String id)
	{
		super(id);
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public abstract void addRoles(Role... roles);
	public abstract Role[] getRoles();
	
	public boolean hasRole(Role role) {
		for(Role role2 : getRoles()) {
			if(role.equals(role2)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() 
	{
		return getClass().getSimpleName() 
				+ " { id:" + getId() 
				+ " roles:" + Arrays.toString(getRoles()) 
				+ " }";
	}
}
	