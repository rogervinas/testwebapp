package com.rogervinas.testwebapp;

import java.io.IOException;

import com.rogervinas.testwebapp.server.Server;
import com.rogervinas.testwebapp.server.ServerImpl;

public class Main
{
	//private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(System.getProperty("port", "8080"));
		Server server = new ServerImpl(port, 10);
		AppModel model = new AppModelTest(10);
		App app = new App(server, model);
		addShutdownHook(app);
		app.start();		
	}
	
	private static void addShutdownHook(App app) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				app.stop();				
			}			
		}));		
	}
}
