package com.rogervinas.testwebapp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class Prova
{
	private static final Logger logger = LoggerFactory.getLogger(Prova.class);
	
	public static void main(String[] args) throws IOException {
		ParesObserver pares = new ParesObserver();
		NonesObserver nones = new NonesObserver();
		
		Observable<Integer> aa = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		Observable<GroupedObservable<Boolean, Integer>> bb = aa.groupBy(value -> value % 2 == 0);
		bb.subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<GroupedObservable<Boolean, Integer>>()
		{

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
			public void onNext(GroupedObservable<Boolean, Integer> t)
			{
				logger.info("onNext " + t);
				if(t.getKey()) {
					t.subscribeOn(Schedulers.newThread()).subscribe(pares);
				} else {
					t.subscribeOn(Schedulers.newThread()).subscribe(nones);
				}
			}			
		});
		System.in.read();
	}
	
	private static class ParesObserver implements Observer<Integer> {

		private static final Logger logger = LoggerFactory.getLogger(ParesObserver.class);
		
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
		public void onNext(Integer t)
		{
			logger.info("onNext: " + t);			
		}		
	}
	
	private static class NonesObserver implements Observer<Integer> {

		private static final Logger logger = LoggerFactory.getLogger(NonesObserver.class);
		
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
		public void onNext(Integer t)
		{
			logger.info("onNext: " + t);			
		}		
	}	
}
