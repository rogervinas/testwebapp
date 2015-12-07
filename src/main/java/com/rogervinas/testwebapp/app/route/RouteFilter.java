package com.rogervinas.testwebapp.app.route;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class RouteFilter extends AbstractFilter
{
	private static final Logger logger = LoggerFactory.getLogger(RouteFilter.class);
	
	private final Pattern methodPattern;
	private final Pattern pathPattern;
	
	public RouteFilter(String methodPattern, String pathPattern) {
		this.methodPattern = Pattern.compile(methodPattern);
		this.pathPattern = Pattern.compile(pathPattern);
	}

	@Override
	public Boolean call(ServerRequest request)
	{
		HttpExchange exchange = request.getExchange();
		boolean matches = 
				methodPattern.matcher(exchange.getRequestMethod()).matches()
				&& pathPattern.matcher(exchange.getRequestURI().getPath()).matches();
		logger.info("ROUTE " + methodPattern + " " + pathPattern + " = " + matches);
		return matches;
	}
}
