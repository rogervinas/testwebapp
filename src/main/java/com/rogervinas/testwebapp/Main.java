package com.rogervinas.testwebapp;

import java.io.IOException;
import java.net.HttpCookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

import com.rogervinas.testwebapp.server.Server;
import com.sun.net.httpserver.HttpExchange;

public class Main
{
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private Server server;
		
	public void start() throws IOException {
		server = new Server();
		Observable<HttpExchange> source = server.createObservable();
		Router router = new Router();
		router.addRoute(new Route("^GET$", "/hello", new HelloWorldObserver()));
		router.addRoute(new Route(".*", ".*", new NotFoundObserver()));
		source.subscribeOn(Schedulers.newThread()).subscribe(new HeadersObserver());
		source.subscribeOn(Schedulers.newThread()).groupBy(router).subscribe(router);
		server.start();
	}
	
	public void stop() {
		server.stop(0);
	}
	
	public static void main(String[] args) throws IOException {
		Main main = new Main();
		main.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				main.stop();				
			}			
		}));
		System.in.read();
		System.exit(0);
	}
	
	private class HeadersObserver implements Observer<HttpExchange>
	{
		private final Logger logger = LoggerFactory.getLogger(HeadersObserver.class);
		
		@Override
		public void onCompleted()
		{
			logger.info("onCompleted");
		}

		@Override
		public void onError(Throwable e)
		{
			logger.error("onError", e);
		}

		@Override
		public void onNext(HttpExchange exchange)
		{
			try {
				logger.info("onNext");
				exchange.getRequestHeaders().forEach((header, list) -> {
					final StringBuffer info = new StringBuffer(header + ": ");
					list.forEach(value -> info.append(value + " "));
					logger.info("Header: " + info.toString());
				});				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	private class NotFoundObserver implements Observer<HttpExchange>
	{
		private final Logger logger = LoggerFactory.getLogger(NotFoundObserver.class);
		
		@Override
		public void onCompleted()
		{
			logger.info("onCompleted");
		}

		@Override
		public void onError(Throwable e)
		{
			logger.error("onError", e);
		}

		@Override
		public void onNext(HttpExchange exchange)
		{
			try {
				logger.info("onNext");
				exchange.sendResponseHeaders(404, 0);
				exchange.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	private class HelloWorldObserver implements Observer<HttpExchange>
	{
		private final Logger logger = LoggerFactory.getLogger(HelloWorldObserver.class);
		
		@Override
		public void onCompleted()
		{
			logger.info("onCompleted");
		}

		@Override
		public void onError(Throwable e)
		{
			logger.error("onError", e);
		}

		@Override
		public void onNext(HttpExchange exchange)
		{
			try {
				logger.info("onNext");
				HttpCookie cookie = new HttpCookie("mysession", "1111");
				exchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
				exchange.sendResponseHeaders(200, 0);
				exchange.getResponseBody().write("<html><body>Hello!</body></html>".getBytes());
				exchange.getResponseBody().close();
				exchange.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
