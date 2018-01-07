package com.fastchat.FastChat;

import com.fastchat.FastChat.client.Login;
import com.fastchat.FastChat.server.Server;

public class Loader {

	private static final String usageString = "Usage: java -jar FastChat.jar [ -s [PORT] [IP] ]";
	private static final String description =
			"This is really cool tool for chatting\nrun without arguments to launch the client";

	private static void usage() {
		System.out.println(usageString);
	}

	private static void help() {
		String helpString = usageString + "\n" +
				description + "\n" +
				"    -h, --help    print this info\n" +
				"    -s, --serve   run FastChat as server\n" +
				"    PORT          the port to serve (default: 1234)\n" +
				"    IP            the IP to serve (default: localhost)\n";

		System.out.println(helpString);
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			Login.main(args);
		} else if (args.length <= 3) {
			if (args[0].equals("-h") || args[0].equals("--help")) {
				help();
			} else if (args[0].equals("-s") || args[0].equals("--serve")) {

				String ip = "localhost";
				int port = 1234;
				if (args.length >= 2) {
					port = Integer.parseInt(args[1]);
				}
				if (args.length == 3) {
					ip = args[2];
				}
				new Server(port, ip);

			}
		} else {
			help();
			System.exit(1);
		}
	}

}
