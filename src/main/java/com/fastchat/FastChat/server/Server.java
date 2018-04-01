package com.fastchat.FastChat.server;

import com.fastchat.FastChat.networking.NetworkCommandRegistry;
import com.fastchat.FastChat.networking.Protocol;
import com.fastchat.FastChat.networking.ProtocolCommands;
import com.fastchat.FastChat.util.Command;
import com.fastchat.FastChat.util.CommandRegistry;

import java.net.*;
import java.util.*;

public class Server implements Runnable {

	private final int MAX_ATTEMPTS = 4;
	private final List<ServerClient> clients = Collections.synchronizedList(new ArrayList<>());

	private final CommandRegistry commandRegistry = new CommandRegistry();
	private final NetworkCommandRegistry networkCommandRegistry = new NetworkCommandRegistry();

	private DatagramSocket socket;

	private final int port;
	private final ArrayList<Integer> response = new ArrayList<>();
	private boolean running;
	private boolean raw;
	@SuppressWarnings("FieldCanBeLocal")
	private Thread run, manage, receive;

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
			socket = new DatagramSocket(this.port, InetAddress.getByName(address));
		} catch (SocketException | UnknownHostException e) {
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
		Scanner in = new Scanner(System.in);
		System.out.println("Type /help to get more information");
		while (running) {
			System.out.print("> ");
			String text;

			try {
				text = in.nextLine();
			} catch (NoSuchElementException e) {
				// Ctrl-D used
				System.exit(0);
				continue;
			}

			if (!text.startsWith("/")) {
				sendToAll(ProtocolCommands.Message.PREFIX + "Server: " + text + Protocol.EOD);
				System.out.println(ProtocolCommands.Message.PREFIX + "Server: " + text + Protocol.EOD);
				continue;
			}

			text = text.substring(1);
			command(text);
		}
		if (!running) in.close();
	}

	/**
	 * @param text command name with arguments
	 */
	private void command(String text) {
		String[] args = text.split(" ");
		String commandName = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		this.commandRegistry.run(commandName, args);
	}

	/**
	 *
	 */
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					synchronized (clients) {
						sendToAll(ProtocolCommands.Ping.PREFIX);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (ServerClient c : clients) {
							if (!response.contains(c.getID())) {
								if (c.attempt >= MAX_ATTEMPTS) {
									disconnectClient(c.getID(), Status.TIMED_OUT);
								} else {
									++c.attempt;
								}
							} else {
								response.remove(new Integer(c.getID()));
								c.attempt = 0;
							}
						}
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
		DatagramPacket packet = Protocol.receive(socket);
		String string = Protocol.parsePacket(packet);

		Protocol.process(packet, networkCommandRegistry,
				() -> {
					if (raw) System.out.println(string);
				},
				() -> System.out.println("[Err]: " + string)
		);
	}

	private void send(String message, InetAddress address, int port) {
		Protocol.send(socket, message, address, port).start();
	}

	private void sendToAll(String string) {
		for (ServerClient client : clients) {
			send(string, client.ip, client.port);
		}
	}

	private void disconnectClient(int id, Status status) {
		for (ServerClient client : clients) {
			if (client.getID() == id) {
				String disc = client.name + "(" + client.getID() + ") ";
				switch (status) {
					case DISCONNECTED:
						disc += "disconnected.";
						break;
					case TIMED_OUT:
						disc += "was timed out.";
						break;
					case KICKED:
						send(ProtocolCommands.Disconnect.PREFIX + "0", client.ip, client.port);
						disc += "was kicked out of server.";
						break;
					case BANNED:
						disc += "was banned!";
						break;
				}
				clients.remove(client);
				ServerClient.removeId(id);
				System.out.println(disc);
				sendToAll(disc);
				break;
			}
		}
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
					System.out.println("Available commands: ");
					commandRegistry.getRegistry().forEach((name, c) ->
							System.out.println("/" + name + (c.getHelp().isEmpty() ? "" : " - " + c.getHelp()))
					);
					return null;
				}),
				new Command("clients", "prints the list of clients", (argv) -> {
					System.out.println("Clients:");
					System.out.println("===================================");
					for (ServerClient serverClient : clients) {
						System.out.println(serverClient);
					}
					System.out.println("===================================");
					return null;
				}),
				new Command("kick", "kick the list of clients", (argv) -> {
					for (String name : argv) {

						int id = -1;
						boolean number = true;
						try {
							id = Integer.parseInt(name);
						} catch (NumberFormatException e) {
							number = false;
						}
						if (number) {
							boolean exists = false;
							for (ServerClient client : clients) {
								if (client.getID() == id) {
									exists = true;
									break;
								}
							}
							if (exists)
								disconnectClient(id, Status.KICKED);
							else
								System.out.println("Client with id " + id + " does not exist");
						} else {
							for (ServerClient client : clients) {
								if (name.equals(client.name)) {
									disconnectClient(client.getID(), Status.KICKED);
									break;
								}
							}
						}
					}
					return null;
				}),
				new Command("raw", "", (argv) -> {
					raw = !raw;
					System.out.println((raw ? "Raw mode enabled" : "Raw mode disabled"));
					return null;
				})
		);

		networkCommandRegistry.add(
				new ProtocolCommands.Ping((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					response.add(Integer.parseInt(data.substring(3)));
				}),
				new ProtocolCommands.Connect((args) -> {
					if (args.length < 3) return;
					String data = args[0];
					InetAddress address;
					try {
						address = InetAddress.getByName(args[1]);
					} catch (UnknownHostException e) {
						System.err.println("Couldn't send connection confirm to " + args[1]);
						return;
					}
					int port = Integer.parseInt(args[2]);
					String name = data.substring(3);
					ServerClient client = new ServerClient(name, address, port);
					synchronized (clients) {
						clients.add(client);
					}
					System.out.println(client + " has connected");
					sendToAll(client.name + "(" + client.getID() + ") has connected");
					send(ProtocolCommands.Connect.PREFIX + client.getID(), client.ip, client.port);
				}),
				new ProtocolCommands.Message((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					System.out.println(data);
					sendToAll(data);
				}),
				new ProtocolCommands.Disconnect((args) -> {
					if (args.length <= 0) return;
					String data = args[0];
					int id = Integer.parseInt(data.substring(3));
					synchronized (clients) {
						disconnectClient(id, Status.DISCONNECTED);
					}
				}),
				new ProtocolCommands.Users((args) -> {
					if (args.length < 3) return;
					InetAddress address;
					try {
						address = InetAddress.getByAddress(args[1].getBytes());
					} catch (UnknownHostException e) {
						System.err.println("Couldn't send user list to " + args[1]);
						return;
					}
					int port = Integer.parseInt(args[2]);
					StringBuilder message = new StringBuilder(ProtocolCommands.Users.PREFIX);
					for (ServerClient client : clients) {
						message.append(client.name).append("(").append(client.getID()).append(")//");
					}
					send(message.toString(), address, port);
				})
		);
	}
}
