package com.rogervinas.testwebapp;

import java.io.IOException;

import rx.Observable;

import com.rogervinas.testwebapp.app.controller.Controller;
import com.rogervinas.testwebapp.app.controller.HeaderLogger;
import com.rogervinas.testwebapp.app.controller.LoginGetController;
import com.rogervinas.testwebapp.app.controller.LoginPostController;
import com.rogervinas.testwebapp.app.controller.LogoutPostController;
import com.rogervinas.testwebapp.app.controller.NotFoundController;
import com.rogervinas.testwebapp.app.controller.PageGetController;
import com.rogervinas.testwebapp.app.route.ContextFilter;
import com.rogervinas.testwebapp.app.route.Filter;
import com.rogervinas.testwebapp.app.route.PageAuthorizedFilter;
import com.rogervinas.testwebapp.app.route.PageFoundFilter;
import com.rogervinas.testwebapp.app.route.PostParamsFilter;
import com.rogervinas.testwebapp.app.route.RequestDoneFilter;
import com.rogervinas.testwebapp.app.route.RouteFilter;
import com.rogervinas.testwebapp.app.route.SessionMapper;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.Server;
import com.rogervinas.testwebapp.server.ServerRequest;

public class App
{
	//private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private final Server server;
	private final AppModel model;
		
	public App(Server server, AppModel model) throws IOException 
	{
		this.server = server;
		this.model = model;
	}
	
	public void start() throws IOException 
	{
		// Create source
		
		Observable<ServerRequest> source = server.observe();
		
		// Create controllers & filters
		
		Controller loginPostController = new LoginPostController(
				model.newUser(null), 
				model.newSession(null)
		);
		
		Controller logoutPostController = new LogoutPostController();
		
		source.subscribe(new HeaderLogger());
				
		Filter requestPending = new RequestDoneFilter().not();
		Filter pageFound = new PageFoundFilter(model.newAccess(null));
		Filter pageAuthorized = new PageAuthorizedFilter();
		Filter pageNotAuthorized = pageAuthorized.not();
		
		Filter sessionFilter = new ContextFilter(Session.class);
		Filter postParamsFilter = new PostParamsFilter();
		
		SessionMapper sessionMapper = new SessionMapper(model.newSession(null));
		sessionMapper.setSessionMaxAge(5 * 60);
		
		// Add routes
		
		source.filter(requestPending)
		.filter(new RouteFilter("^GET$", ".*"))
		.filter(pageFound)
		.map(sessionMapper)
		.filter(sessionFilter)
		.filter(pageAuthorized)
		.subscribe(new PageGetController());
		
		source.filter(requestPending)
		.filter(new RouteFilter("^GET$", ".*"))
		.filter(pageFound)
		.map(sessionMapper)
		.filter(pageNotAuthorized)
		.subscribe(new LoginGetController());
		
		source.filter(requestPending)
		.filter(new RouteFilter("^POST$", "/login"))
		.filter(postParamsFilter)
		.subscribe(loginPostController);
		
		source.filter(requestPending)
		.filter(new RouteFilter("^POST$", "/logout"))
		.map(sessionMapper)
		.filter(sessionFilter)
		.filter(postParamsFilter)
		.subscribe(logoutPostController);
		
		source.filter(requestPending)
		.subscribe(new NotFoundController());
		
		// Start server
		
		server.start();
	}

	public void stop() {
		server.stop(0);
	}
}
