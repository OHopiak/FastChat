package com.fastchat.FastChat.server;

import com.fastchat.FastChat.beans.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerClient extends User {

	private static final ArrayList<Integer> identifiers = new ArrayList<>();

	private ServerClient(String name, InetAddress ip, int port, Socket socket) throws IOException {
		setName(name);
		setPort(port);
		setIp(ip);
		setSocket(socket);
		setID(createId());
	}

	ServerClient(Socket socket) throws IOException {
		this(socket.getInetAddress().getHostAddress() + ":" + socket.getPort(), socket);
	}

	private ServerClient(String name, Socket socket) throws IOException {
		this(name, socket.getInetAddress(), socket.getPort(), socket);
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
		return (getName() != null ? getName() : "NONAME") + "(" + getID() + ")";
//				+ (ip != null ? ip + ":" : "NOADDRESS") + port;
	}
}
