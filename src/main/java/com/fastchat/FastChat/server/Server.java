package com.fastchat.FastChat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
	
	private final int MAX_ATTEMPTS = 4;
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private ArrayList<Integer> response = new ArrayList<Integer>();
	
	private DatagramSocket socket;
	private int port;
	private Thread run, manage, send, receive;
	private boolean running;
	private boolean raw;
	private static final String[] commands = { "clients", /*"cls",*/ "help", "kick", "raw", "exit" };
	
	private static enum Status {
		DISCONNECTED, TIMED_OUT, KICKED, BANNED
	};
	
	public Server(int port) {
		System.out.println("Server started at port " + port);
		this.port = port;
		try {
			socket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}
	
	public void run() {
		running = true;
		manageClients();
		receive();
		Scanner in = new Scanner(System.in);
		System.out.println("Type /help to get some information\n");
		while (running) {
			String text = in.nextLine();
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
	
	private void command(String text) {
		switch (text.split(" ")[0]) {
			case "raw":
				raw = !raw;
				System.out.println((raw ? "Raw mode enabled" : "Raw mode disabled"));
				break;
//			case "cls":
//				System.out.flush();
//				Process p;
//				try {
//					p = Runtime.getRuntime().exec("cls");
//					p.waitFor();
//				} catch (IOException | InterruptedException e) {
//					System.err.println("Cannot run this command");
//				}
//				break;
			case "clients":
				System.out.println("Clients:");
				System.out.println("===================================");
				for (ServerClient serverClient : clients) {
					System.out.println(serverClient);
				}
				System.out.println("===================================");
				break;
			case "kick":
				String name = text.split(" ")[1];
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
				break;
			case "help":
				System.out.println("Available commands: ");
				for (int i = 0; i < commands.length; i++) {
					System.out.println("/" + commands[i]);
				}
				break;
			case "exit":
				System.out.print("Do you really want to exit? (y/n)   ");
				Scanner input = new Scanner(System.in);
				char option = input.nextLine().toLowerCase().charAt(0);
				if (option == 'y') {
					System.exit(0);
				}
				break;
			default:
				System.out.println("Command does not exist!");
				return;
		}
		
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
					for (int i = 0; i < clients.size(); i++) {
						ServerClient c = clients.get(i);
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
			} else {
				continue;
			}
		}
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
			String message = "/u/";
			for (ServerClient client : clients) {
				message += client.name + "(" + client.getID() + ")//";
			}
			send(message, packet.getAddress(), packet.getPort());
		} else {
			System.out.println(string);
		}
	}
	
}
