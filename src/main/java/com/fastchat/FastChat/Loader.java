package com.fastchat.FastChat;

import com.fastchat.FastChat.client.LoginInterface;
import com.fastchat.FastChat.client.gui.LoginGUI;
import com.fastchat.FastChat.server.Server;
import com.fastchat.FastChat.util.Localization;

import java.awt.*;

class Loader {

	private static final String usageString = "Usage: java -jar FastChat.jar [ -s [PORT] [IP] ]";
	private static final String description =
			"This is really cool tool for chatting\nrun without arguments to launch the client";

//	private static Class clientClass;

	private static void help() {
		String helpString = usageString + "\n" +
				description + "\n" +
				"    -h, --help    print this info\n" +
				"    -s, --serve   run FastChat as server\n" +
				"    PORT          the port to serve (default: 1234)\n" +
				"    IP            the IP to serve (default: localhost)\n";

		System.out.println(helpString);
	}

	/**
	 * <pre>
	 * Backend TODOs
	 * TODO: Text Box for messages instead of Text Field
	 * TODO: Limit width of message history Text Area
	 * TODO: Show messages in separate boxes (or at least in different colors per user)
	 * TODO: Group networking in a protocol
	 * TODO: Add settings frame
	 * TODO: Add sound to play when message comes
	 * TODO: Make a single class (JavaBean) for User
	 * TODO: Move networking from UDP to TCP
	 * TODO: Make tests for existing classes
	 * FIXME: Add cyrillic support in Windows
	 * FIXME: Add either message limit, or enable sending messages of any length
	 *
	 * Frontend TODOs:
	 * TODO: Pick main color scheme
	 * TODO: Make a dark and light theme
	 * TODO: (optional) use Material UI
	 * TODO: Reshape all components
	 * TODO: Add custom font (must support any languages)
	 * </pre>
	 *
	 * @param args arguments of application
	 */
	public static void main(String[] args) {

		Localization.autoSetLocale();                //English
//		Localization.setLocale(new Locale("uk"));	//Ukrainian
//		Localization.setLocale(new Locale("nb"));	//Norwegian

		if (args.length == 0) {
//			EventQueue.invokeLater(Login::getCurrentLogin);
			EventQueue.invokeLater(() -> {
				System.out.println("[*] Starting Login");
				LoginInterface login = new LoginGUI();

			});
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
