package com.fastchat.FastChat.networking;

import java.util.HashMap;

/**
 * A registry to hold network commands
 */
public class NetworkCommandRegistry {
	private HashMap<String, NetworkCommand> registry = new HashMap<>();

	/**
	 * adds commands to the registry, using command shortcut as a key
	 * @param commands Commands to be added to registry
	 */
	public void add(NetworkCommand... commands) {
		for (NetworkCommand command : commands) {
			registry.put(command.getShortcut(), command);
		}
	}

	/**
	 * Gets a network command based on the shortcut
	 * @param shortcut of a command in registry
	 * @return command with this shortcut or null if not found
	 */
	public NetworkCommand get(String shortcut) {
		return registry.getOrDefault(shortcut, null);
	}
}
