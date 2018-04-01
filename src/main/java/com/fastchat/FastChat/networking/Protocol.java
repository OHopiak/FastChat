package com.fastchat.FastChat.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.function.Supplier;

public class Protocol {
	public static final String EOD = "/e/";

	public static final Runnable IGNORE = () -> {
	};

	public static void process(DatagramPacket packet, NetworkCommandRegistry networkCommandRegistry) {
		process(packet, networkCommandRegistry, IGNORE, IGNORE);
	}

	public static void process(DatagramPacket packet, NetworkCommandRegistry networkCommandRegistry, Runnable onSuccess) {
		process(packet, networkCommandRegistry, onSuccess, IGNORE);
	}

	public static void process(String string, NetworkCommandRegistry networkCommandRegistry) {
		process(string, networkCommandRegistry, IGNORE, IGNORE);
	}

	public static void process(String string, NetworkCommandRegistry networkCommandRegistry, Runnable onSuccess) {
		process(string, networkCommandRegistry, onSuccess, IGNORE);
	}

	public static void process(DatagramPacket packet, NetworkCommandRegistry networkCommandRegistry,
							   Runnable onSuccess, Runnable onError) {
		String string = new String(packet.getData());
		string = string.split(EOD)[0];
		onSuccess.run();
		NetworkCommand cmd = networkCommandRegistry.get(string.charAt(1) + "");

		//FIXME hardcoded crappy if statement should be replaced
		if (cmd == null || string.length() < 3 || string.charAt(0) != '/' || string.charAt(2) != '/')
			onError.run();
		else
			cmd.exec(string, packet.getAddress().getHostAddress(), Integer.toString(packet.getPort()));
	}

	public static void process(String string, NetworkCommandRegistry networkCommandRegistry,
							   Runnable onSuccess, Runnable onError) {
		string = string.split(EOD)[0];
		onSuccess.run();
		NetworkCommand cmd = networkCommandRegistry.get(string.charAt(1) + "");

		//FIXME hardcoded crappy if statement should be replaced
		if (cmd == null || string.length() < 3 || string.charAt(0) != '/' || string.charAt(2) != '/')
			onError.run();
		else
			cmd.exec(string);
	}

	public static String parsePacket(DatagramPacket packet) {
		return new String(packet.getData()).split(EOD)[0];
	}

	public static DatagramPacket receive(DatagramSocket socket) {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}

	private static Thread send(DatagramSocket socket, final byte[] data, final InetAddress address, final int port) {
		return new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public static Thread send(DatagramSocket socket, String message, final InetAddress address, final int port) {
		message += EOD;
		return send(socket, message.getBytes(), address, port);
	}

	public static Thread listen(Supplier<Boolean> isRunning, Runnable tick) {
		return new Thread("Listen") {
			public void run() {
				while (isRunning.get()) {
					tick.run();
				}
			}
		};
	}


}
