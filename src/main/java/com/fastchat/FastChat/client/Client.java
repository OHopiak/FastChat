package com.fastchat.FastChat.client;

import com.fastchat.FastChat.beans.User;
import com.fastchat.FastChat.networking.ProtocolCommands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends User {
	
	private boolean running = false, kicked = false, banned = false;

	Client(String name, int port) {
		setName(name);
		setPort(port);
	}

	void openConnection(String address) throws IOException {
		setIp(InetAddress.getByName(address));
		setSocket(new Socket(address, getPort()));
		send(getName());
	}

	public void send(String message) {
		getOut().println(message);
		getOut().flush();
//		Protocol.send(socket, message, ip, port).start();
	}

	public void disconnect(int id) {
		running = false;
		//socket.close();
		if (kicked || banned) {
//			((LoginGUI)LoginGUI.getCurrentLogin()).reset();
//			((ClientGUI)LoginGUI.getClientInterface()).getFrame().setVisible(false);
//			JOptionPane.showMessageDialog(null,
//					(kicked ? "You were kicked out of server\nTry to reconnect again"
//							: "You were banned on this server!\nYou can't reconnect unless the server restarts!"),
//					"Warning!", JOptionPane.WARNING_MESSAGE);
			kicked = false;
			banned = false;
		} else {
			send(ProtocolCommands.Disconnect.PREFIX + id);
			System.exit(0);
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setKicked(boolean kicked) {
		this.kicked = kicked;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}
}
