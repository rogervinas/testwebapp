package com.rogervinas.testwebapp.model.mem;

import com.rogervinas.testwebapp.model.Role;

public class RoleMem extends Role
{
	private final GenericMemDb<Role> db;

	public RoleMem(String id, GenericMemDb<Role> db)
	{
		super(id);
		this.db = db;
	}

	@Override
	public RoleMem create(String id)
	{
		return new RoleMem(id, db);
	}	
	
	@Override
	public Role load(String id)
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
