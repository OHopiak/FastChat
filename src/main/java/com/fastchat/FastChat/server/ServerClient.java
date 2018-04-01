package com.fastchat.FastChat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerClient {

	private static final ArrayList<Integer> identifiers = new ArrayList<>();
	public final Socket socket;
	private final InetAddress ip;
	private final int ID;
	public int attempt = 0;
	public final int port;
	public String name;
	public BufferedReader reader;
	public PrintWriter out;


	public int getID() {
		return ID;
	}

	private ServerClient(String name, InetAddress ip, int port, Socket socket) throws IOException {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.ID = createId();
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream());
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
		return (name != null ? name : "NONAME") + "(" + ID + ")";
//				+ (ip != null ? ip + ":" : "NOADDRESS") + port;
	}
}
