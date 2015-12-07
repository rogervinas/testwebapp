package com.rogervinas.testwebapp.app.controller;

import java.io.IOException;

import com.rogervinas.testwebapp.app.view.LogoutView;
import com.rogervinas.testwebapp.app.view.TemplateValues;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;


public class LogoutPostController extends AbstractController
{
	//private final Logger logger = LoggerFactory.getLogger(LogoutPostController.class);

	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();
		PostParams params = request.contextGet(PostParams.class);
		if(params == null) return false;
		TemplateValues values = new TemplateValues();
		Session session = request.contextGet(Session.class);
		session.delete();
		exchange.getResponseHeaders().add("Set-Cookie", "sessionId=0");
		values.put("message", String.format("User <b>%s</b> successfully logged out", session.getUser().getId()));
		LogoutView.get().render(200, exchange, values);
		return true;					
	}
}