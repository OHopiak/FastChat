package com.fastchat.FastChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	
	private String name;
	private int port;
	private int ID;
	private DatagramSocket socket;
	private InetAddress ip;
	private Thread send;
	private boolean running;
	
	
	protected boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		message = message.split("/e/")[0];
		return message;
	}
	
	protected void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	protected void send(String message) {
		message += "/e/";
		send(message.getBytes());
	}
	
	protected void disconnect() {
		String connection = name + " disconnected from " + socket.getLocalAddress() + ":" + socket.getLocalPort();
		send(connection);
	}
	
	public String getName() {
		return name;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getPort() {
		return port;
	}
	
	public InetAddress getIp() {
		return ip;
	}
	
	public Client(String name, int port) {
		super();
		this.name = name;
		this.port = port;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
