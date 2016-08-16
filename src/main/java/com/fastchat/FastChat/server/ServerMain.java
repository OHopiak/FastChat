package com.fastchat.FastChat.server;

public class ServerMain {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java -jar FastChatServer.jar [port]");
			return;
		}
		int port = Integer.parseInt(args[0]);
		System.out.println(port);
		new Server(port);
	}
	
}
