package com.rogervinas.testwebapp.app.controller;

import java.io.IOException;

import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class RedirectController extends AbstractController
{
	//private final Logger logger = LoggerFactory.getLogger(RedirectController.class);
	
	private final String location;
	
	public RedirectController(String location)
	{
		this.location = location;
	}

	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();
		exchange.getResponseHeaders().add("Location",  location);
		exchange.sendResponseHeaders(302, 0);
		return true;				
	}
}