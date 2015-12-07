package com.rogervinas.testwebapp.app.route;

import rx.functions.Func1;

import com.rogervinas.testwebapp.server.ServerRequest;

public interface Filter extends Func1<ServerRequest, Boolean>
{
	public Filter not();
}
