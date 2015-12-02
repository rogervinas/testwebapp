package com.rogervinas.testwebapp;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Main
{
	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
		server.start();
		System.out.println(String.format("Server listening on http://localhost:%d", server.getAddress().getPort()));
		System.out.println("Press ENTER to stop");
		System.in.read();
		System.out.println("Stopping ...");
		server.stop(0);
		System.out.println("Stopped");
	}
}
