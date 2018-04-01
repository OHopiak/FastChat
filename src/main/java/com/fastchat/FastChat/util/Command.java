package com.fastchat.FastChat.util;


public class Command {
	private final String help;
	private final String name;

	//it's Interface created for body
	private final EnhancedCallable<String, String> body;


	public EnhancedCallable getBody() {
		return body;
	}


	/**
	 * @param name a name of the command
	 * @param body body of the command
	 */
	public Command(String name, EnhancedCallable<String, String> body) {
		this(name, "", body);
	}

	public Command(String name, String help, EnhancedCallable<String, String> body) {
		this.name = name;
		this.help = help;
		this.body = body;
	}

	/**
	 * @param args command arguments
	 */
	public void exec(String... args) {
		try {
			body.call(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getHelp() {
		return help;
	}

	public String getName() {
		return name;
	}


}



