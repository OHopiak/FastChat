package com.fastchat.FastChat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
	private DatagramSocket socket;
	private int port;
	private Thread run, manage, send, receive;
	private boolean running;
	
	public Server(int port) {
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
	}
	
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					// managing
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
	
	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		string = string.split("/e/")[0];
		if (string.startsWith("/c/")) {
			String name = string.substring(3);
			ServerClient client = new ServerClient(name, packet.getAddress(), packet.getPort());
			clients.add(client);
			System.out.println(client + " has connected");
			send("/c/" + client.getID(), client.ip, client.port);
		} else if (string.startsWith("/m/")) {
			System.out.println(string);
			sendToAll(string);
		} else {
			System.out.println(string);
		}
	}

	
}
