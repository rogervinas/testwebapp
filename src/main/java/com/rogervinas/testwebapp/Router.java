package com.rogervinas.testwebapp;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observer;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

import com.sun.net.httpserver.HttpExchange;

public class Router implements Observer<GroupedObservable<Route,HttpExchange>>, Func1<HttpExchange, Route>
{
	private static final Logger logger = LoggerFactory.getLogger(Router.class);
	
	private final Collection<Route> routes = new ArrayList<Route>();
	
	public void addRoute(Route route) {
		routes.add(route);
	}

	@Override
	public Route call(HttpExchange exchange)
	{		
		for(Route route : routes) {
			if(route.matches(exchange.getRequestMethod(), exchange.getRequestURI().getPath())) {
				return route;
			}			
		}
		return null;
	}

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
	public void onNext(GroupedObservable<Route, HttpExchange> observable)
	{
		Route route = observable.getKey();
		logger.info("Creating new route: " + route);
		observable.subscribe(route.getObserver());
	}
}
