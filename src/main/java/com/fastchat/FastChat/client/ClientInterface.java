package com.fastchat.FastChat.client;

import com.fastchat.FastChat.util.Localization;

public abstract class ClientInterface implements Runnable {

	Client client;
	private Thread run, listen;
	private OnlineUsers users;

	private String name, address;
	private int port;

	private String[] userArr = {"Name(id)"};

	ClientInterface() {
		this("Anon", "localhost", 1234);
	}

	ClientInterface(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;

//		init();
	}

	protected abstract void onBreak();

	protected void init() {
		client = new Client(name, port);
		client.setRunning(true);

		console(String.format("%s %s:%d...", Localization.get("client_attempting_to_connect"), address, port));

		boolean connected = client.openConnection(address);

		if (!connected) {
			String client_connection_failed = Localization.get("client_connection_failed");
			System.err.println(client_connection_failed);
			console(client_connection_failed);
			onBreak();
		} else {
			this.serve();
		}
		client.send(("/c/" + name));
	}

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

	private void serve() {
		run = new Thread(this, "Run");
		run.start();
	}

	public Thread getRun() {
		return run;
	}

	public Thread getListen() {
		return listen;
	}

	public OnlineUsers getUsers() {
		return users;
	}

	public String[] getUserArr() {
		return userArr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
