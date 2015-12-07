package com.rogervinas.testwebapp.model.mem;

import com.rogervinas.testwebapp.model.Session;

public class SessionMem extends Session
{
	private final GenericMemDb<Session> db;

	public SessionMem(String id, GenericMemDb<Session> db)
	{
		super(id);
		this.db = db;
	}

	@Override
	public SessionMem create(String id)
	{
		return new SessionMem(id, db);
	}
	
	@Override
	public Session load(String id)
	{
		return db.load(id);
	}

	@Override
	public void save()
	{
		db.save(this);
	}

	@Override
	public void delete()
	{
		db.delete(this);
	}
}
