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
	
	private final User userTmp;
	private final Session sessionTmp;
	private int sessionMaxAge = 3600;
	
	public LoginPostController(User userTmp, Session sessionTmp) {
		this.userTmp = userTmp;
		this.sessionTmp = sessionTmp;
	}
	
	/*
	 * @param maxAge max age in seconds
	 */
	public void setSessionMaxAge(int maxAge) {
		this.sessionMaxAge = maxAge;
	}
	
	@Override
	protected boolean onNextImpl(ServerRequest request) throws IOException
	{
		HttpExchange exchange = request.getExchange();
		PostParams params = request.contextGet(PostParams.class);
		if(params == null) return false;
		Session session = sessionTmp.create(UUID.randomUUID().toString());
		session.setMaxAge(sessionMaxAge);
		String username = params.getProperty("user");
		String password = params.getProperty("password");
		User user = userTmp.load(username);
		TemplateValues values = new TemplateValues();					
		if(user == null || !user.getPassword().equals(password)) {
			values.put("redirect", params.getProperty("redirect"));
			values.put("message", String.format("User %s not found or incorrect password", username));
			LoginView.get().render(401, exchange, values);
		} else {
			session.setUser(user);
			session.save();
			exchange.getResponseHeaders().add("Location", params.getProperty("redirect"));
			exchange.getResponseHeaders().add("Set-Cookie", String.format("%s=%s", Session.class.getSimpleName(), session.getId()));
			exchange.sendResponseHeaders(302, 0);
		}
		return true;
	}
}