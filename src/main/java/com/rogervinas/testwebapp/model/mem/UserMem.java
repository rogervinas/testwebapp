package com.rogervinas.testwebapp.model.mem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.rogervinas.testwebapp.model.Role;
import com.rogervinas.testwebapp.model.User;

public class UserMem extends User
{
	private final GenericMemDb<User> db;
	private final Set<Role> roles = new HashSet<Role>();
	
	public UserMem(String id, GenericMemDb<User> db)
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
	public UserMem create(String id)
	{
		return new UserMem(id, db);
	}
	
	@Override
	public User load(String id)
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
