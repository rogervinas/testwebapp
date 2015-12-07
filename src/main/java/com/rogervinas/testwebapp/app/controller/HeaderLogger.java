package com.rogervinas.testwebapp.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observer;

import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class HeaderLogger implements Observer<ServerRequest>
{
	private static final Logger logger = LoggerFactory.getLogger(HeaderLogger.class);
	
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
	public void onNext(ServerRequest request)
	{
		HttpExchange exchange = request.getExchange();
		logger.info(String.format("REQUEST %s %s", exchange.getRequestMethod(), exchange.getRequestURI().getPath()));
		exchange.getRequestHeaders().forEach((header, list) -> {
			list.forEach(value -> {
				logger.info("REQUEST header: " + header + " " + value);
			});						
		});		
	}
}
