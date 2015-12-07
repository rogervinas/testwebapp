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
		boolean filter =
				key == null
				? request.contextGet(clazz) != null
				: request.contextGet(key, clazz) != null;
		logger.info("found " + clazz.getSimpleName() + "." + key + " = " + filter);
		return filter;
	}
}
