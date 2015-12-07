package com.rogervinas.testwebapp.app.route;

import com.rogervinas.testwebapp.server.ServerRequest;

public abstract class AbstractFilter implements Filter
{
	public Filter not() {
		return new AbstractFilter() {
			@Override
			public Boolean call(ServerRequest request)
			{
				return ! AbstractFilter.this.call(request);
			}			
		};
	}
}
