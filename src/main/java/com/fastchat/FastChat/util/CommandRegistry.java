package com.fastchat.FastChat.util;

import java.util.HashMap;

public class CommandRegistry {
	private final HashMap<String, Command> registry = new HashMap<>();

	public void add(Command... commands) {
		for (Command command : commands)
			registry.put(command.getName(), command);
	}

	public void run(String name, String... args) {
		Command c = registry.get(name);

		if (c != null) {
			c.exec(args);
		} else {
			throw new IllegalArgumentException("Command does not exist!");
		}
	}

	public HashMap<String, Command> getRegistry() {
		return registry;
	}
}
