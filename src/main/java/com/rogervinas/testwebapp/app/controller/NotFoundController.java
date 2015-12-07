package com.rogervinas.testwebapp.app.controller;

import java.io.IOException;

import com.rogervinas.testwebapp.app.view.NotFoundView;
import com.rogervinas.testwebapp.app.view.TemplateValues;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class NotFoundController extends AbstractController
{
	//private final Logger logger = LoggerFactory.getLogger(NotFoundController.class);

	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();
		TemplateValues values = new TemplateValues();
		values.put("page", exchange.getRequestURI().getPath());
		NotFoundView.get().render(404, exchange, values);
		return true;				
	}		
}