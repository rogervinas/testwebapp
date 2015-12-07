package com.rogervinas.testwebapp.server;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class ServerRequest
{
	private final HttpExchange exchange;
	private boolean done = false;
	private final Map<String,Object> context = new HashMap<String,Object>();
	
	public ServerRequest(HttpExchange exchange)
	{
		this.exchange = exchange;
	}
	
	public HttpExchange getExchange()
	{
		return exchange;		
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public void setDone()
	{
		done = true;
	}
	
	public void contextPut(String key, Object obj)
	{
		context.put(key, obj);
	}
	
	public void contextPut(Object obj, Class<?> clazz)
	{
		context.put(clazz.getName(), obj);
	}	
	
	@SuppressWarnings("unchecked")
	public <T> T contextGet(String key, Class<T> clazz)
	{
		return (T) context.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T contextGet(Class<T> clazz)
	{
		return (T) context.get(clazz.getName());
	}	
}
