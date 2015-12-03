package com.rogervinas.testwebapp.model;

import java.util.HashSet;
import java.util.Set;

public class User
{
	private String username;
	private String name;
	private Set<Role> roles = new HashSet<Role>(); 
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}	
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}	
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public boolean hasRole(Role role) {
		return roles.contains(role);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof User) {
			return ((User) obj).username.equals(username);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}	
}
	