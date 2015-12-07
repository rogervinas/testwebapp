package com.rogervinas.testwebapp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Subscriber;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerImpl implements Server
{
	private static final Logger logger = LoggerFactory.getLogger(ServerImpl.class);
	
	private final HttpServer server;	
	private final Collection<ServerListener> listeners = new ArrayList<ServerListener>();
	
	public ServerImpl(int port, int threadPoolSize) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.setExecutor(new ScheduledThreadPoolExecutor(threadPoolSize));
		server.createContext("/", new HttpHandler()
		{			
			@Override
			public void handle(HttpExchange exchange)
			{
				ServerRequest request = new ServerRequest(exchange);
				listeners.forEach(listener -> listener.onRequest(request));
			}
		});		
	}
	
	public void start() {
		logger.info("Starting ...");
		server.start();
		logger.info(String.format("Listening on http://localhost:%d", server.getAddress().getPort()));
		logger.info("Started!");
	}
	
	public void stop(int delay) {
		logger.info("Stopping ...");	
		listeners.forEach(listener -> listener.onStop());
		server.stop(delay);
		logger.info("Stopped");		
	}
	
	public Observable<ServerRequest> observe() {
		return Observable.create(new Observable.OnSubscribe<ServerRequest>()
		{
			@Override
			public void call(Subscriber<? super ServerRequest> subscriber)
			{
				ServerListener listener = new ServerListener() {
					public void onRequest(ServerRequest request) {
						if(!subscriber.isUnsubscribed()) {
							subscriber.onNext(request);
						}
					}
					
					public void onStop() {
						subscriber.onCompleted();
					}
				};
				ServerImpl.this.listeners.add(listener);
			}
		});		
	}
	
	private interface ServerListener {
		public void onRequest(ServerRequest request);
		public void onStop();
	}	
}
