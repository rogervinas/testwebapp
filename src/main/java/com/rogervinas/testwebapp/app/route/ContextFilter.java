package com.rogervinas.testwebapp.app.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.server.ServerRequest;

public class ContextFilter extends AbstractFilter
{
	private static final Logger logger = LoggerFactory.getLogger(ContextFilter.class);
	
	private final String key;
	private final Class<?> clazz;
	
	public ContextFilter(String key, Class<?> clazz) {
		this.key = key;
		this.clazz = clazz;
	}
	
	public ContextFilter(Class<?> clazz) {
		this.key = null;
		this.clazz = clazz;
	}
	
	@Override
	public Boolean call(ServerRequest request)
	{
		boolean filter;
		if(key != null) {
			filter = request.contextGet(key, clazz) != null;
			logger.info(String.format("%s %s in context", key, filter ? "found" : "NOT found"));
		} else {
			filter = request.contextGet(clazz) != null;
			logger.info(String.format("%s %s in context", clazz.getSimpleName(), filter ? "found" : "NOT found"));
		}		
		return filter;
	}
}
