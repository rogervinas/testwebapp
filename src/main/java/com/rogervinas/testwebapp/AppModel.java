package com.rogervinas.testwebapp;

import com.rogervinas.testwebapp.model.Access;
import com.rogervinas.testwebapp.model.Role;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.model.User;

public interface AppModel
{
	public User newUser(String id);
	public Access newAccess(String id);
	public Session newSession(String id);
	public Role newRole(String id);
}
