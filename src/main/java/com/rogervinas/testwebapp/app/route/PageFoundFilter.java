package com.rogervinas.testwebapp.app.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Access;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class PageFoundFilter extends AbstractFilter
{
	private static final Logger logger = LoggerFactory.getLogger(SessionMapper.class);
	
	private final Access accessTmp;
	private final String contextKey = "pageFound";
	
	public PageFoundFilter(Access accessTmp) {
		this.accessTmp = accessTmp;
	}
	
	@Override
	public Boolean call(ServerRequest request)
	{
		Boolean pageFound = request.contextGet(contextKey, Boolean.class);
		if(pageFound == null) {
			HttpExchange exchange = request.getExchange();
			Access access = accessTmp.load(exchange.getRequestURI().getPath());
			pageFound = access != null;
			request.contextPut(access, Access.class);
			request.contextPut(contextKey, pageFound);
		}			
		logger.info("PageFound: " + request.getExchange().getRequestURI().getPath() + " = " + pageFound);
		return pageFound;
	}

}
