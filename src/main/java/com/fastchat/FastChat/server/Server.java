package com.fastchat.FastChat.server;

import com.fastchat.FastChat.networking.NetworkCommandRegistry;
import com.fastchat.FastChat.networking.Protocol;
import com.fastchat.FastChat.networking.ProtocolCommands;
import com.fastchat.FastChat.util.Command;
import com.fastchat.FastChat.util.CommandRegistry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server implements Runnable {

	private static final String CARET = "> ";

	private final CommandRegistry commandRegistry = new CommandRegistry();
	private final NetworkCommandRegistry networkCommandRegistry = new NetworkCommandRegistry();
	private final HashMap<Integer, ServerClient> clients = new HashMap<>();

	private final int port;
	private boolean running;
	private boolean raw;
	private ServerSocket serverSocket;
	@SuppressWarnings("FieldCanBeLocal")
	private Thread run, manage, receive, console;

	private enum Status {
		DISCONNECTED, TIMED_OUT, KICKED, BANNED
	}

	/**
	 * @param port
	 * @param address
	 */
	public Server(int port, String address) {
		System.out.printf("Server started at %s:%d\n", address, port);
		this.port = port;
		try {
			serverSocket = new ServerSocket(port, 100, InetAddress.getByName(address));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		initCommands();
		run.start();
	}

	/**
	 *
	 */
	public void run() {
		running = true;
		manageClients();
		receive();
		startConsole();
		console("Type /help to get more information");
	}

	/**
	 * @param text command name with arguments
	 */
	private void command(String text) {
		String[] args = text.split(" ");
		String commandName = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		try {
			commandRegistry.run(commandName, args);
		} catch (IllegalArgumentException e) {
			System.out.println("Command \"" + commandName + "\" doesn't exist");
		}
	}

	/**
	 *
	 */
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (clients) {
						clients.forEach((id, c) -> {
							if (c.socket.isClosed())
								disconnectClient(c.getID(), Status.TIMED_OUT);
						});
					}
				}
			}
		};
		manage.start();
	}

	private void receive() {
		receive = Protocol.listen(() -> running, this::tick);
		receive.start();
	}

	private void tick() {
		try {
			Socket socket = serverSocket.accept();
			ServerClient client = new ServerClient(socket);
			clients.put(client.getID(), client);
			getClientThread(client).start();
		} catch (IOException e) {
			System.err.println("I/O error: " + e);
		}
//		DatagramPacket packet = Protocol.receive(clientSocket);
//		String string = Protocol.parsePacket(packet);
//
//		Protocol.process(packet, networkCommandRegistry,
//				() -> {
//					if (raw) System.out.println(string);
//				},
//				() -> System.out.println("[Err]: " + string)
//		);
	}

	private void startConsole() {
		console = new Thread(() -> {
			Scanner in = new Scanner(System.in);
			while (running) {
				String text;

				try {
					text = in.nextLine();
				} catch (NoSuchElementException e) {
					// Ctrl-D used
					System.exit(0);
					continue;
				}

				if (!text.startsWith("/")) {
					sendToAll("Server: " + text);
					console("Server: " + text);
					continue;
				}

				text = text.substring(1);
				command(text);
			}
			if (!running) in.close();
		}, "Console");
		console.start();
	}

	private void send(String message, ServerClient client) {
		client.out.println(message);
		client.out.flush();
	}

	private void console(Object message) {
		System.out.print("\r" + message.toString() + "\n" + CARET);
	}

	private void sendToAll(String string) {
		clients.forEach((id, client) -> send(ProtocolCommands.Message.PREFIX + string, client));
	}

	private void disconnectClient(int id, Status status) {
		ServerClient client = clients.getOrDefault(id, null);
		if (client == null) {
			return;
		}
		String disc = client.name + "(" + client.getID() + ") ";
		switch (status) {
			case DISCONNECTED:
				disc += "disconnected.";
				break;
			case TIMED_OUT:
				disc += "was timed out.";
				break;
			case KICKED:
				send(ProtocolCommands.Disconnect.PREFIX + "0", client);
				disc += "was kicked out of server.";
				break;
			case BANNED:
				disc += "was banned!";
				break;
		}
		try {
			client.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		synchronized (clients) {
			clients.remove(id);
		}
		ServerClient.removeId(id);
		console(disc);
		sendToAll(disc);

	}

	public int getPort() {
		return port;
	}

	private void initCommands() {

		commandRegistry.add(
				new Command("exit", (argv) -> {
					System.out.print("Do you really want to exit? (y/n)   ");
					Scanner input = new Scanner(System.in);
					char option = input.nextLine().toLowerCase().charAt(0);
					if (option == 'y') {
						System.exit(0);
					}
					return null;
				}),
				new Command("help", (argv) -> {
					console("Available commands: ");
					commandRegistry.getRegistry().forEach((name, c) ->
							console("/" + name + (c.getHelp().isEmpty() ? "" : " - " + c.getHelp()))
					);
					return null;
				}),
				new Command("clients", "prints the list of clients", (argv) -> {
					console("Clients:");
					console("===================================");
					clients.forEach((id, client) -> console(client));
					console("===================================");
					return null;
				}),
				new Command("kick", "kick the list of clients", (argv) -> {
					for (String name : argv) {

						int id = -1;
						try {
							id = Integer.parseInt(name);
						} catch (NumberFormatException ignored) {
						}
						if (clients.containsKey(id))
							disconnectClient(id, Status.KICKED);
						else
							console("Client with id " + id + " does not exist");
					}
					return null;
				}),
				new Command("raw", "", (argv) -> {
					raw = !raw;
					console((raw ? "Raw mode enabled" : "Raw mode disabled"));
					return null;
				})
		);

		networkCommandRegistry.add(
//				new ProtocolCommands.Connect((args) -> {
//					if (args.length < 3) return;
//					String data = args[0];
//					InetAddress address;
//					try {
//						address = InetAddress.getByName(args[1]);
//					} catch (UnknownHostException e) {
//						System.err.println("Couldn't send connection confirm to " + args[1]);
//						return;
//					}
//					int port = Integer.parseInt(args[2]);
//					String name = data.substring(3);
//					ServerClient client = null;
//					try {
//						client = new ServerClient(name, address, port, null);
//					} catch (IOException e) {
//						e.printStackTrace();
//						return;
//					}
//					synchronized (clients) {
//						clients.add(client);
//					}
//				}),
				new ProtocolCommands.Message((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					int id = Integer.parseInt(args[1]);
					if (clients.containsKey(id)) {
						data = clients.get(id) + ":" + data;
						console(data);
						sendToAll(data);
					}
				}),
				new ProtocolCommands.Disconnect((args) -> {
					if (args.length < 2) return;
					int id = Integer.parseInt(args[1]);
					disconnectClient(id, Status.DISCONNECTED);
				}),
				new ProtocolCommands.Users((args) -> {
					if (args.length < 2) return;
					int id = Integer.parseInt(args[1]);
					ServerClient current = clients.getOrDefault(id, null);
					if (current == null) return;
					StringBuilder message = new StringBuilder(ProtocolCommands.Users.PREFIX);
					clients.forEach((key, client) ->
							message.append(client.name)
									.append("(")
									.append(client.getID())
									.append(")//"));
					send(message.toString(), current);
				})
		);

	}

	/**
	 * Creates a thread for connection with client
	 *
	 * @param client that has connected
	 * @return thread that keeps the connection with client
	 */
	private Thread getClientThread(ServerClient client) {
		return new Thread(() -> {
			try {
				client.name = client.reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			console(client + " has connected");
			sendToAll(client.name + "(" + client.getID() + ") has connected");
			send(ProtocolCommands.Connect.PREFIX + client.getID(), client);

			while (!client.socket.isClosed()) {
				try {
					String line = client.reader.readLine();
					if (line == null) return;
					Protocol.process(client, line, networkCommandRegistry,
							() -> {
								if (raw) console(line);
							},
							() -> console("[Err]: " + line)
					);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});
	}
}