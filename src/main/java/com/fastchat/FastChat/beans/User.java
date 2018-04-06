package com.fastchat.FastChat.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class User {
	private String name;
	private InetAddress ip;
	private int port;
	private int ID;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter out;

	protected User() {
		this.name = "";
		this.ip = null;
		this.port = 0;
		this.ID = 0;
		this.socket = null;
		this.reader = null;
		this.out = null;
	}

//	public User(String name, InetAddress ip, int port, int ID, Socket socket) throws IOException {
//		this.name = name;
//		this.ip = ip;
//		this.port = port;
//		this.ID = ID;
//		this.socket = socket;
//		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//		this.out = new PrintWriter(socket.getOutputStream());
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetAddress getIp() {
		return ip;
	}

	protected void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public Socket getSocket() {
		return socket;
	}

	protected void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream());

	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public UserBean toBean() {
		return new UserBean(name, ID);
	}
}
