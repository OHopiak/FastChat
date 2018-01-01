package com.fastchat.FastChat.server;

public class ServerMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java -jar FastChatServer.jar port [address]");
			return;
		}
		int port = Integer.parseInt(args[0]);
		String address;
		if (args.length >= 2) {
			address = args[1];
		} else {
			address = "localhost";
		}
		new Server(port, address);
	}

}
