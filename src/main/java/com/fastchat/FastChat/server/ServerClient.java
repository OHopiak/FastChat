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
	private static ArrayList<Integer> identifiers = new ArrayList<>();
	
	public int getID() {
		return ID;
	}

	ServerClient(String name, InetAddress ip, int port) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.ID = createId();
	}
	
	private static int createId() {
		int id = new Random().nextInt(10000);
		if (!(identifiers.contains(id) || id <= 0)) {
			identifiers.add(id);
			return id;
		} else {
			return createId();
		}
	}
	
	public static void removeId(int id) {
		for (Integer integer : identifiers) {
			if (integer.equals(id)) {
				identifiers.remove(integer);
				return;
			}
		}
	}
	
	@Override
	public String toString() {
		return (name != null ? name : "NONAME") + "(" + ID + ")" + (ip != null ? ip + ":" : "NOADDRESS") + port;
	}
}
