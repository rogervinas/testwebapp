package com.rogervinas.testwebapp.app.controller;

import java.io.IOException;

import com.rogervinas.testwebapp.app.view.LoginView;
import com.rogervinas.testwebapp.app.view.TemplateValues;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class LoginGetController extends AbstractController
{
	//private final Logger logger = LoggerFactory.getLogger(LoginGetController.class);

	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();		
		TemplateValues values = new TemplateValues();
		String path = exchange.getRequestURI().getPath();
		values.put("redirect", path);
		Session session = request.contextGet(Session.class);
		int status;
		if(session != null) {
			status = 403;
			values.put("header", "Page forbidden");
			values.put("message", String.format("User <b>%s</b> does not have access to <b>%s</b>, please request another page or login as another user", session.getUser().getId(), path));
		} else {			
			status = 200;
			values.put("header", "Login Page");
			values.put("message", String.format("Please login to access <b>%s</b>", path));
		}
		LoginView.get().render(status, exchange, values);
		return true;				
	}
}