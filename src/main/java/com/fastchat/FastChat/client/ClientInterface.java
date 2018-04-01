package com.fastchat.FastChat.client;

import com.fastchat.FastChat.networking.NetworkCommandRegistry;
import com.fastchat.FastChat.networking.Protocol;
import com.fastchat.FastChat.networking.ProtocolCommands;
import com.fastchat.FastChat.util.Localization;

import java.io.IOException;

public abstract class ClientInterface implements Runnable {

	Client client;
	private OnlineUsers users;
	private String[] userArr = {"Name(id)"};
	private final NetworkCommandRegistry networkCommandRegistry = new NetworkCommandRegistry();
	private String name, address;
	private int port;
	@SuppressWarnings("FieldCanBeLocal")
	private Thread run, listen;

	ClientInterface() {
		this("Anon", "localhost", 1234);
	}

	ClientInterface(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;

		initCommands();
//		init();
	}

	protected abstract void onBreak();

	void init() {
		client = new Client(name, port);
		client.setRunning(true);

		console(String.format("%s %s:%d...", Localization.get("client_attempting_to_connect"), address, port));

		boolean connected = true;
		try {
			client.openConnection(address);
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
		}

		if (!connected) {
			String client_connection_failed = Localization.get("client_connection_failed");
			System.err.println(client_connection_failed);
			console(client_connection_failed);
			onBreak();
		} else {
			this.serve();
		}
//		client.send((ProtocolCommands.Connect.PREFIX + name));
		users = new OnlineUsers();
	}

	protected abstract void send(String message);

	abstract void console(String message);

	public void run() {
		listen();
	}

	private void listen() {
		listen = Protocol.listen(client::isRunning, () -> {
			try {
				String message = client.getReader().readLine();
				if (message == null) {
					System.err.println("Server ended session");
					System.exit(0);
				}
				if (!message.isEmpty())
					Protocol.process(message, networkCommandRegistry,
							Protocol.IGNORE,
							() -> console("Error: " + message)
					);
			} catch (IOException ignored) {
			}
		});
		listen.start();
	}

	private void serve() {
		run = new Thread(this, "Run");
		run.start();
	}

	private void initCommands() {
		networkCommandRegistry.add(
//				new ProtocolCommands.Ping((args) -> client.send(ProtocolCommands.Ping.PREFIX + client.getID())),
				new ProtocolCommands.Connect((args) -> {
					if (args.length < 3) return;
					String data = args[0];
					client.setID(Integer.parseInt(data));
					console("Connection is set up successfully... ID is " + client.getID());
				}),
				new ProtocolCommands.Message((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					console(data.substring(3));
				}),
				new ProtocolCommands.Disconnect((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					data = data.substring(3);
					if (data.startsWith("0")) {
						client.setKicked(true);
						client.disconnect(client.getID());
					} else {
						client.setBanned(true);
						client.disconnect(client.getID());
					}
				}),
				new ProtocolCommands.Users((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					data = data.substring(3);
					userArr = data.split("//");
					users.updateList(userArr);
					users.setVisible(true);
				})
		);
	}

	public Client getClient() {
		return client;
	}

	String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String getAddress() {
		return address;
	}

	int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
