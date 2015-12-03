package com.rogervinas.testwebapp;

import java.util.regex.Pattern;

import rx.Observer;

import com.sun.net.httpserver.HttpExchange;

public class Route
{
	private final Pattern methodPattern;
	private final Pattern pathPattern;
	private final Observer<HttpExchange> observer;
	
	public Route(String methodRegex, String pathRegex, Observer<HttpExchange> observer) {
		this.methodPattern = Pattern.compile(methodRegex);
		this.pathPattern = Pattern.compile(pathRegex);
		this.observer = observer;
	}
	
	public boolean matches(String method, String path) {
		return methodPattern.matcher(method).matches() && pathPattern.matcher(path).matches();
	}
	
	public Observer<HttpExchange> getObserver() {
		return observer;		
	}
	
	public String toString() {
		return methodPattern + " " + pathPattern;
	}
}
