package com.rogervinas.testwebapp.app.controller;

import java.io.IOException;
import java.util.UUID;

import com.rogervinas.testwebapp.app.view.LoginView;
import com.rogervinas.testwebapp.app.view.TemplateValues;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.model.User;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;


public class LoginPostController extends AbstractController
{
	//private final Logger logger = LoggerFactory.getLogger(LoginPostController.class);
	
	private final User nullUser;
	private final Session nullSession;
	
	public LoginPostController(User nullUser, Session nullSession) {
		this.nullUser = nullUser;
		this.nullSession = nullSession;
	}
	
	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();
		PostParams params = request.contextGet(PostParams.class);
		if(params == null) return false;
		Session session = nullSession.create(UUID.randomUUID().toString());
		String username = params.getProperty("user");
		String password = params.getProperty("password");
		User user = nullUser.load(username);
		TemplateValues values = new TemplateValues();					
		if(user == null || !user.getPassword().equals(password)) {
			values.put("redirect", params.getProperty("redirect"));
			values.put("message", String.format("User %s not found or incorrect password", username));
			exchange.sendResponseHeaders(200, 0);
			LoginView.get().render(exchange, values);
		} else {
			session.setUser(user);
			session.save();
			exchange.getResponseHeaders().add("Location", params.getProperty("redirect"));
			exchange.getResponseHeaders().add("Set-Cookie", "sessionId=" + session.getId());
			exchange.sendResponseHeaders(302, 0);
		}
		return true;
	}
}