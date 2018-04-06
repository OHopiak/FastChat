package com.fastchat.FastChat.client;

public abstract class OnlineUsersInterface {
//	List<String> users;

	protected abstract void init();

	protected abstract void update(String[] users);

	protected abstract void show();
}
