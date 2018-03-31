package com.fastchat.FastChat.networking;

import java.util.HashMap;

public class NetworkCommandRegistry {

	private HashMap<String, NetworkCommand> registry = new HashMap<>();

	/**
	 * @param commands
	 */
	public void add(NetworkCommand... commands) {
		for (NetworkCommand command : commands) {
			registry.put(command.getName(), command);
		}
	}

	/**
	 * @param shortcut
	 * @return
	 */
	public NetworkCommand get(String shortcut) {
		return registry.getOrDefault(shortcut, null);
	}
}
