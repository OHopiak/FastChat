package com.fastchat.FastChat.client;

import com.fastchat.FastChat.networking.ProtocolCommands;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	private final String name;
	private final int port;
	private int ID;
	private Socket socket;
	private InetAddress ip;
	private boolean running = false, kicked = false, banned = false;

	private BufferedReader reader;
	private PrintWriter out;


	Client(String name, int port) {
		super();
		this.name = name;
		this.port = port;
	}

	void openConnection(String address) throws IOException {
		socket = new Socket(address, port);
		ip = InetAddress.getByName(address);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		send(name);
	}

	void send(String message) {
		out.println(message);
		out.flush();
//		Protocol.send(socket, message, ip, port).start();
	}

	void disconnect(int id) {
		running = false;
		//socket.close();
		if (kicked || banned) {
//			((LoginGUI)LoginGUI.getCurrentLogin()).reset();
//			((ClientGUI)LoginGUI.getClientInterface()).getFrame().setVisible(false);
			JOptionPane.showMessageDialog(null,
					(kicked ? "You were kicked out of server\nTry to reconnect again"
							: "You were banned on this server!\nYou can't reconnect unless the server restarts!"),
					"Warning!", JOptionPane.WARNING_MESSAGE);
			kicked = false;
			banned = false;
		} else {
			send(ProtocolCommands.Disconnect.PREFIX + id);
			System.exit(0);
		}
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getPort() {
		return port;
	}

	public InetAddress getIp() {
		return ip;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isKicked() {
		return kicked;
	}

	public void setKicked(boolean kicked) {
		this.kicked = kicked;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public PrintWriter getOut() {
		return out;
	}
}
