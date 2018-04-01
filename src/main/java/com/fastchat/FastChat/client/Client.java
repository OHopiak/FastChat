package com.fastchat.FastChat.client;

import com.fastchat.FastChat.networking.Protocol;
import com.fastchat.FastChat.networking.ProtocolCommands;

import javax.swing.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	private final String name;
	private final int port;
	private int ID;
	private DatagramSocket socket;
	private InetAddress ip;
	private boolean running = false, kicked = false, banned = false;

	Client(String name, int port) {
		super();
		this.name = name;
		this.port = port;
	}

	boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	void send(String message) {
		Protocol.send(socket, message, ip, port).start();
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

	public DatagramSocket getSocket() {
		return socket;
	}
}
