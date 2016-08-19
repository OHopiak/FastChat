package com.fastchat.FastChat.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

public class ServerClient {
	
	public String name;
	public InetAddress ip;
	public int port;
	private final int ID;
	public int attempt = 0;
	private static ArrayList<Integer> identifiers = new ArrayList<Integer>();
	
	public int getID() {
		return ID;
	}
	
	public ServerClient(String name, InetAddress ip, int port) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.ID = createId();
	}
	
	private int createId() {
		int id = new Random().nextInt();
		if (!identifiers.contains(id)) {
			identifiers.add(id);
			return id;
		} else {
			return createId();
		}
	}
	
	@Override
	public String toString() {
		return "Client " + (name != null ? name + " - " : "") + (ip != null ? ip + ":" : "") + port + " ID: " + ID;
	}
}
