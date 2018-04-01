package com.fastchat.FastChat.networking;

import com.fastchat.FastChat.util.EnhancedRunnable;

public class ProtocolCommands {
	static String prefix(String shortcut) {
		return "/" + shortcut + "/";
	}

	public static class Ping extends NetworkCommand {
		static final String NAME = "ping";
		static final String SHORTCUT = "i";
		public static final String PREFIX = prefix(SHORTCUT);

		public Ping(EnhancedRunnable<String> receive) {
			super(NAME, SHORTCUT, receive);
		}
	}

	public static class Connect extends NetworkCommand {
		static String NAME = "connect";
		static String SHORTCUT = "c";
		public static final String PREFIX = prefix(SHORTCUT);

		public Connect(EnhancedRunnable<String> receive) {
			super(NAME, SHORTCUT, receive);
		}
	}

	public static class Message extends NetworkCommand {
		static String NAME = "message";
		static String SHORTCUT = "m";
		public static final String PREFIX = prefix(SHORTCUT);

		public Message(EnhancedRunnable<String> receive) {
			super(NAME, SHORTCUT, receive);
		}
	}

	public static class Disconnect extends NetworkCommand {
		static String NAME = "disconnect";
		static String SHORTCUT = "d";
		public static final String PREFIX = prefix(SHORTCUT);

		public Disconnect(EnhancedRunnable<String> receive) {
			super(NAME, SHORTCUT, receive);
		}
	}

	public static class Users extends NetworkCommand {
		static String NAME = "disconnect";
		static String SHORTCUT = "d";
		public static final String PREFIX = prefix(SHORTCUT);

		public Users(EnhancedRunnable<String> receive) {
			super(NAME, SHORTCUT, receive);
		}
	}
}
