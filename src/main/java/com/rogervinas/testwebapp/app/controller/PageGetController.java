package com.rogervinas.testwebapp.app.controller;

import java.io.IOException;

import com.rogervinas.testwebapp.app.view.PageView;
import com.rogervinas.testwebapp.app.view.TemplateValues;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class PageGetController extends AbstractController
{
	//private final Logger logger = LoggerFactory.getLogger(PageGetController.class);

	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();
		TemplateValues values = new TemplateValues();
		String path = exchange.getRequestURI().getPath();
		Session session = request.contextGet(Session.class);
		values.put("user", session.getUser().getId());
		values.put("page", path);
		PageView.get().render(200, exchange, values);
		return true;				
	}
}