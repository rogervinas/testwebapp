package com.rogervinas.testwebapp.server;

import rx.Observable;

public interface Server
{
	public void start();
	public void stop(int delay);
	public Observable<ServerRequest> observe();
}
