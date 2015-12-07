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
	
	public SessionMapper(Session sessionTmp) {
		this.sessionTmp = sessionTmp;
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
					logger.info(String.format("Session %s has expired", session.getId()));
				} else {
					session.resetCreationTime();
					session.save();
					request.contextPut(session, Session.class);
					logger.info(String.format("Session %s is valid", session.getId()));
				}				
			} else {
				logger.info("Session not found in request");
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
					if(
							nameValue!=null 
							&& nameValue.length==2 
							&& nameValue[0].trim().equals(Session.class.getSimpleName())
					) {
						return nameValue[1].trim();
					}
				}
			}
		}
		return null;
	}
}
