package com.rogervinas.testwebapp.app.route;

import com.rogervinas.testwebapp.server.ServerRequest;

public class RequestDoneFilter extends AbstractFilter
{
	@Override
	public Boolean call(ServerRequest request)
	{
		synchronized(request) {
			return request.isDone();
		}
	}
}
