package com.rogervinas.testwebapp;

public class HelperHttpClients
{
	private final HelperHttpClient[] helperHttpClients;
	
	public HelperHttpClients(int count) {
		helperHttpClients = new HelperHttpClient[count];
		for(int i=0; i<helperHttpClients.length; i++) {
			helperHttpClients[i] = new HelperHttpClient();
		}		
	}
	
	public HelperHttpClient getHelperHttpClient(int client) {
		return helperHttpClients[client];
	}
	
	public HelperHttpClient[] getHelperHttpClients() {
		return helperHttpClients;
	}
	
	public void close() throws Exception {
		for(HelperHttpClient helperHttpClient : helperHttpClients) {
			helperHttpClient.getHttpClient().close();
		}
	}
}
