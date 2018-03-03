package com.fastchat.FastChat.server;

import com.fastchat.FastChat.util.Command;
import com.fastchat.FastChat.util.CommandRegistry;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server implements Runnable {

	private final int MAX_ATTEMPTS = 4;
	private List<ServerClient> clients = new ArrayList<>();

	private CommandRegistry commandRegistry = new CommandRegistry();

	private DatagramSocket socket;

	private int port;
	private ArrayList<Integer> response = new ArrayList<>();
	private boolean running;
	private boolean raw;
	@SuppressWarnings("FieldCanBeLocal")
	private Thread run, manage, send, receive;

	private enum Status {
		DISCONNECTED, TIMED_OUT, KICKED, BANNED
	}

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
				sendToAll("/m/Server: " + text + "/e/");
				System.out.println("/m/Server: " + text + "/e/");
				continue;
			}

			text = text.substring(1);
			command(text);
		}
		if (!running) in.close();
	}

	/**
	 * example: "kick 12345432 2345 3456 3456 34567 34567"
	 * example - split: ["kick", "12345432", "2345", "3456", "3456", "34567", "34567"]
	 * example - command name: "kick"
	 * example - argv: ["12345432", "2345", "3456", "3456", "34567", "34567"]
	 *
	 * @param text
	 */
	private void command(String text) {
		String[] args = text.split(" ");
		String commandName = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		this.commandRegistry.run(commandName, args);

	}

	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					sendToAll("/i/");
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
		};
		manage.start();
	}

	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		string = string.split("/e/")[0];
		if (raw) System.out.println(string);
		if (string.startsWith("/c/")) {
			String name = string.substring(3);
			ServerClient client = new ServerClient(name, packet.getAddress(), packet.getPort());
			clients.add(client);
			System.out.println(client + " has connected");
			sendToAll(client.name + "(" + client.getID() + ") has connected");
			send("/c/" + client.getID(), client.ip, client.port);
		} else if (string.startsWith("/m/")) {
			System.out.println(string);
			sendToAll(string);
		} else if (string.startsWith("/d/")) {
			int id = Integer.parseInt(string.substring(3));
			disconnectClient(id, Status.DISCONNECTED);
		} else if (string.startsWith("/i/")) {
			response.add(Integer.parseInt(string.substring(3)));
		} else if (string.startsWith("/u/")) {
			StringBuilder message = new StringBuilder("/u/");
			for (ServerClient client : clients) {
				message.append(client.name).append("(").append(client.getID()).append(")//");
			}
			send(message.toString(), packet.getAddress(), packet.getPort());
		} else {
			System.out.println(string);
		}
	}

	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}

	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void send(String message, InetAddress ip, int port) {
		message += "/e/";
		send(message.getBytes(), ip, port);
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
						send("/d/0", client.ip, client.port);
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

		this.commandRegistry.add(new Command("exit", (argv) -> {
			System.out.print("Do you really want to exit? (y/n)   ");
			Scanner input = new Scanner(System.in);
			char option = input.nextLine().toLowerCase().charAt(0);
			if (option == 'y') {
				System.exit(0);
			}
			return null;
		}));

		this.commandRegistry.add(new Command("help", (argv) -> {
			System.out.println("Available commands: ");
			commandRegistry.getCommands().forEach((name, c) ->
					System.out.println("/" + name + (c.getHelp().isEmpty() ? "" : " - " + c.getHelp()))
			);
			return null;
		}));

		this.commandRegistry.add(new Command("clients", "prints the list of clients", (argv) -> {
			System.out.println("Clients:");
			System.out.println("===================================");
			for (ServerClient serverClient : clients) {
				System.out.println(serverClient);
			}
			System.out.println("===================================");
			return null;
		}));

		this.commandRegistry.add(new Command("kick", "kick the list of clients", (argv) -> {
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
		}));

		this.commandRegistry.add(new Command("raw", "", (argv) -> {
			raw = !raw;
			System.out.println((raw ? "Raw mode enabled" : "Raw mode disabled"));
			return null;
		}));
	}
}
