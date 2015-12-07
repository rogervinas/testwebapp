package com.rogervinas.testwebapp.model;

import java.util.Arrays;

public abstract class Access extends ActiveRecord<Access>
{
	public Access(String id)
	{
		super(id);
	}
	
	public abstract void addRoles(Role... roles);	
	public abstract Role[] getRoles();
	
	public boolean hasAccess(Role... roles) {
		for(Role role1 : getRoles()) {
			for(Role role2 : roles) {
				if(role1.equals(role2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasPublicAccess() {
		return getRoles().length == 0;
	}
	
	public String toString() {
		return getClass().getSimpleName() 
				+ "{ id:" + getId() 
				+ " roles:" + Arrays.toString(getRoles()) 
				+ " }";
	}
}
