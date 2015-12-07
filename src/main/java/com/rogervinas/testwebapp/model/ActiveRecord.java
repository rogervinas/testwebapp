package com.rogervinas.testwebapp.model;

public abstract class ActiveRecord<T> implements Cloneable
{
	private final String id;
	
	public ActiveRecord(String id)
	{
		this.id = id;
	}
		
	public String getId() {
		return id;
	}
	
	public T load() {
		return load(getId());
	}
	
	public abstract T create(String id);
	public abstract T load(String id);
	public abstract void save();
	public abstract void delete();
	
	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof ActiveRecord) {		
			return this.getClass().isAssignableFrom(obj.getClass())
					&& ((ActiveRecord<?>) obj).getId().equals(this.getId()); 
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " { id:" + getId() + " }";
	}

	@Override
	public Object clone()
	{
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}	
}
