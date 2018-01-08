package com.fastchat.FastChat.client;

public abstract class ClientInterface implements Runnable {

	protected Client client;
	private Thread run, listen;
	private OnlineUsers users;

	private String[] userArr = {"Name(id)"};

	protected abstract void send(String message);

	abstract void console(String message);

	public void run() {
		listen();
	}

	private void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while (client.isRunning()) {
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.substring(3)));
						console("Connection is setted up successfully... ID is " + client.getID());
					} else if (message.startsWith("/m/")) {
						console(message.substring(3));
					} else if (message.startsWith("/i/")) {
						client.send("/i/" + client.getID());
					} else if (message.startsWith("/d/")) {
						message = message.substring(3);
						if (message.startsWith("0")) {
							client.setKicked(true);
							client.disconnect(client.getID());
						} else {
							client.setBanned(true);
							client.disconnect(client.getID());
						}
					} else if (message.startsWith("/u/")) {
						message = message.substring(3);
						userArr = message.split("//");
						users.updateList(userArr);
					} else {
						console("Server: " + message);
					}
				}
			}
		};
		listen.start();
	}

}
