package com.rogervinas.testwebapp.model;

public class Role
{
	private final String name;
	
	public Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Role) {
			return ((Role) obj).name.equals(name);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
