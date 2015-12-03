package com.rogervinas.testwebapp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Subscriber;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server
{
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	
	private final HttpServer server;	
	private final Collection<ServerListener> listeners = new ArrayList<ServerListener>();
	
	public Server() throws IOException {
		server = HttpServer.create(new InetSocketAddress(3000), 0);
		server.createContext("/", new HttpHandler()
		{			
			@Override
			public void handle(HttpExchange exchange)
			{
				listeners.forEach(listener -> listener.onExchange(exchange));
			}
		});		
	}
	
	public void start() {
		logger.info("Starting ...");
		server.start();
		logger.info(String.format("Server listening on http://localhost:%d", server.getAddress().getPort()));
		logger.info("Started!");
	}
	
	public void stop(int delay) {
		logger.info("Stopping ...");		
		listeners.forEach(listener -> listener.onStop());
		server.stop(delay);
		logger.info("Stopped ...");		
	}
	
	public Observable<HttpExchange> createObservable() {
		return Observable.create(new Observable.OnSubscribe<HttpExchange>()
		{
			@Override
			public void call(Subscriber<? super HttpExchange> subscriber)
			{
				ServerListener listener = new ServerListener() {
					public void onExchange(HttpExchange exchange) {
						if(!subscriber.isUnsubscribed()) {
							subscriber.onNext(exchange);
						}
					}
					
					public void onStop() {
						subscriber.onCompleted();
					}
				};
				Server.this.listeners.add(listener);
			}
		});		
	}
	
	private interface ServerListener {
		public void onExchange(HttpExchange exchange);
		public void onStop();
	}	
}
