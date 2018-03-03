package com.fastchat.FastChat.util;

import java.util.HashMap;

public class CommandRegistry {
	private HashMap<String, Command> commands = new HashMap<>();


	public void add(Command command){
		commands.put(command.getName(),command);
	}

	public void run(String name, String... args){
		Command c = commands.get(name);

		if (c != null) {
			c.exec(args);
		} else {
			throw new IllegalArgumentException("Command does not exist!");
		}
	}

	public HashMap<String, Command> getCommands() {
		return commands;
	}
}
