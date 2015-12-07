package com.rogervinas.testwebapp.model;

public abstract class Session extends ActiveRecord<Session>
{	
	private User user;
	private long expires = System.currentTimeMillis();
	
	public Session(String id)
	{
		super(id);
	}

	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public void setExpires(long time) {
		this.expires = time;
	}
	
	public long getExpires() {
		return this.expires;
	}
	
	/*
	 * Sets session expiration time to now + <code>age</code> seconds
	 * @param age time to add to expiration time in seconds
	 */
	public void setMaxAge(long age) {
		setExpires(System.currentTimeMillis() + age * 1000);
	}
	
	public boolean hasExpired() {
		return this.expires < System.currentTimeMillis();
	}
	
	public String toString() 
	{
		return getClass().getSimpleName() 
				+ " { id:" + getId() 
				+ " user:" + user
				+ " expirationTime:" + expires
				+ " }";
	}
}
