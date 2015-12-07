package com.rogervinas.testwebapp.app.route;

import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class SessionMapper implements Mapper
{
	private static final Logger logger = LoggerFactory.getLogger(SessionMapper.class);
	
	private final Session sessionTmp;
	private long sessionMaxAge = 60 * 60; 
	
	public SessionMapper(Session sessionTmp) {
		this.sessionTmp = sessionTmp;
	}
	
	public void setSessionMaxAge(long age) {
		this.sessionMaxAge = age;
	}
	
	@Override
	public ServerRequest call(ServerRequest request)
	{
		HttpExchange exchange = request.getExchange();
		String sessionId = getSessionId(exchange.getRequestHeaders());
		if(sessionId != null) {
			Session session = sessionTmp.load(sessionId);
			if(session != null) {
				if(session.hasExpired()) {
					session.delete();
					logger.info("session has expired " + session);
				} else {
					session.setMaxAge(sessionMaxAge);
					logger.info("found session " + session);
					request.contextPut(session, Session.class);
				}				
			} else {
				logger.info("session not found");
			}
		}
		return request;
	}	
	
	private String getSessionId(Headers headers)
	{
		List<String> cookies = headers.get("Cookie");
		if(cookies != null) {
			for(String cookie : cookies) {
				StringTokenizer tokenizer = new StringTokenizer(cookie, ";");
				while(tokenizer.hasMoreTokens()) {
					String[] nameValue = tokenizer.nextToken().split("=");
					if(nameValue!=null && nameValue.length==2 && nameValue[0].trim().equals("sessionId")) {
						return nameValue[1].trim();
					}
				}
			}
		}
		return null;
	}
}
