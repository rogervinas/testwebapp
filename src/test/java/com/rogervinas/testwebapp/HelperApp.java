package com.rogervinas.testwebapp;

import com.rogervinas.testwebapp.server.Server;
import com.rogervinas.testwebapp.server.ServerImpl;

public class HelperApp
{
	private final int usersCount;
	private final String url;
	private final App app;
	
	public HelperApp(int usersCount) throws Exception {
		this.usersCount = usersCount;
		int port = Integer.parseInt(System.getProperty("port", "8081"));
	    Server server = new ServerImpl(port, 5);
	    this.url = String.format("http://localhost:%d/", port);
	    AppModel model = new AppModelTest(usersCount);
	    this.app = new App(server, model);
	}
	
	public void start() throws Exception {
		app.start();
	}
	
	public void stop() {
		app.stop();
	}

	public int getUsersCount()
	{
		return usersCount;
	}

	public String getUrl()
	{
		return url;
	}
	
	public String getUrl(String path) {
		return url + path;
	}
}
