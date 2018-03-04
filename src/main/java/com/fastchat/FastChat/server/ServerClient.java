package com.fastchat.FastChat.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

class ServerClient {

	private static final ArrayList<Integer> identifiers = new ArrayList<>();
	public final String name;
	public final InetAddress ip;
	private final int ID;
	public int attempt = 0;
	public final int port;
	
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
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		return (name != null ? name : "NONAME") + "(" + ID + ")" + (ip != null ? ip + ":" : "NOADDRESS") + port;
	}
}
