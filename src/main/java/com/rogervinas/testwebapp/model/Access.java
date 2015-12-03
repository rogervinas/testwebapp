package com.rogervinas.testwebapp.model;

public class Access
{
	private String method;
	private String path;
	private Role role;
	private boolean granted;
		
	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public void setRole(Role role)
	{
		this.role = role;
	}

	public boolean isGranted()
	{
		return granted;
	}

	public void setGranted(boolean granted)
	{
		this.granted = granted;
	}
}
