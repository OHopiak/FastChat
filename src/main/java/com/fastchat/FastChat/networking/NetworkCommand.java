package com.fastchat.FastChat.networking;

import com.fastchat.FastChat.util.EnhancedRunnable;

public class NetworkCommand {

	private String name;
	private String shortcut;
	private EnhancedRunnable<String> process;

	public NetworkCommand(String name, String shortcut, EnhancedRunnable<String> process) {
		this.name = name;
		this.shortcut = shortcut;
		this.process = process;
	}

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
