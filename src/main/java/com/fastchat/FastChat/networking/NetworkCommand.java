package com.fastchat.FastChat.networking;

import com.fastchat.FastChat.util.EnhancedRunnable;

/**
 * A command that sends data through network
 */
public class NetworkCommand {

	private String name;
	private String shortcut;
	private EnhancedRunnable<String> process;

	/**
	 * @param name     to describe the command
	 * @param shortcut that is used in networking and as a key in registry
	 * @param process  that is executed when command is executed
	 */
	NetworkCommand(String name, String shortcut, EnhancedRunnable<String> process) {
		this.name = name;
		this.shortcut = shortcut;
		this.process = process;
	}

	/**
	 * Executes current network command with params
	 * @param args Arguments to pass to the command
	 * @throws RuntimeException if the command returned any errors
	 */
	public void exec(String... args) throws RuntimeException {
		try {
			process.run(args);
		} catch (Exception e) {
			throw new RuntimeException("Command '" + name + "' exited with error: " + e.getMessage());
		}
	}

	public String getShortcut() {
		return shortcut;
	}

	public String getName() {
		return name;
	}
}
