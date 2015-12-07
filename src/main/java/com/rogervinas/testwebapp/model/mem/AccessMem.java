package com.rogervinas.testwebapp.model.mem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.rogervinas.testwebapp.model.Access;
import com.rogervinas.testwebapp.model.Role;

public class AccessMem extends Access
{
	private final GenericMemDb<Access> db;
	private final Set<Role> roles = new HashSet<Role>();
	
	public AccessMem(String id, GenericMemDb<Access> db)
	{
		super(id);
		this.db = db;
	}

	@Override
	public void addRoles(Role... roles)
	{
		this.roles.addAll(Arrays.asList(roles));
	}

	@Override
	public Role[] getRoles()
	{
		return this.roles.toArray(new Role[] {});
	}	
	
	@Override
	public Access create(String id)
	{
		return new AccessMem(id, db);
	}	

	@Override
	public Access load(String id)
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
