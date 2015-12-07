package com.rogervinas.testwebapp.model;

public abstract class Session extends ActiveRecord<Session>
{	
	private User user;
	private long creationTime = System.currentTimeMillis();
	private int maxAge;

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
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public void resetCreationTime() {
		setCreationTime(System.currentTimeMillis());
	}
	
	public void setCreationTime(long time) {
		this.creationTime = time;
	}
		
	public int getMaxAge() {
		return maxAge;
	}
	
	/*
	 * @param maxAge max age in seconds
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public boolean hasExpired() {
		return creationTime + maxAge*1000 < System.currentTimeMillis();
	}

	public String toString() 
	{
		return getClass().getSimpleName() 
				+ " { id:" + getId() 
				+ " user:" + user
				+ " creationTime:" + creationTime
				+ " maxAge:" + maxAge
				+ " }";
	}
}
