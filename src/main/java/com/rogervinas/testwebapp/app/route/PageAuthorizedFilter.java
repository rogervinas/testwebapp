package com.rogervinas.testwebapp.app.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.Access;
import com.rogervinas.testwebapp.model.Session;
import com.rogervinas.testwebapp.server.ServerRequest;

public class PageAuthorizedFilter extends AbstractFilter
{
	private static final Logger logger = LoggerFactory.getLogger(SessionMapper.class);

	private final String contextKey = "pageAuthorized";
	
	@Override
	public Boolean call(ServerRequest request)
	{
		Boolean pageAuthorized = request.contextGet(contextKey, Boolean.class);
		if(pageAuthorized == null) {
			pageAuthorized = getPageAuthorized(request); 
			request.contextPut(contextKey, pageAuthorized);
		}
		logger.info("PageAuthorized: " + request.getExchange().getRequestURI().getPath() + " = " + pageAuthorized);
		return pageAuthorized;
	}
	
	private Boolean getPageAuthorized(ServerRequest request) {
		Access access = request.contextGet(Access.class);
		if(access == null) return false;
		if(access.hasPublicAccess()) return true;
		Session session = request.contextGet(Session.class);
		if(session == null) return false;
		return access.hasAccess(session.getUser().getRoles());
	}
}
