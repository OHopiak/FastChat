package com.fastchat.FastChat.networking;

import com.fastchat.FastChat.server.ServerClient;

import java.util.function.Supplier;

public class Protocol {
	public static final Runnable IGNORE = () -> {
	};

	public static void process(ServerClient client, String message, NetworkCommandRegistry networkCommandRegistry,
							   Runnable onSuccess, Runnable onError) {
		NetworkCommand cmd = message.length() >= 3 ? networkCommandRegistry.get(message.charAt(1) + "") : null;
		//FIXME hardcoded crappy if statement should be replaced
		if (cmd == null || message.charAt(0) != '/' || message.charAt(2) != '/')
			onError.run();
		else {
			onSuccess.run();
			if (client == null)
				cmd.exec(message.substring(3));
			else
				cmd.exec(message.substring(3), Integer.toString(client.getID()));
		}
	}

	public static void process(String message, NetworkCommandRegistry networkCommandRegistry,
							   Runnable onSuccess, Runnable onError) {
		process(null, message, networkCommandRegistry, onSuccess, onError);
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
