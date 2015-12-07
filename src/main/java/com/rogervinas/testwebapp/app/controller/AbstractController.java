package com.rogervinas.testwebapp.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.server.ServerRequest;

public abstract class AbstractController implements Controller
{
	private final Logger logger = LoggerFactory.getLogger(AbstractController.class);
	
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
	public void onNext(ServerRequest request)
	{
		synchronized(request) {
			if(!request.isDone()) {
				try {
					logger.info("onNext");
					if(onNextImpl(request)) {
						request.getExchange().close();
						request.setDone();
					}
				} catch(Exception e) {
					onError(e);
				}
			}
		}
	}
	
	protected abstract boolean onNextImpl(ServerRequest request) throws Exception;
}
