package com.fastchat.FastChat.client;

public abstract class LoginInterface {
	ClientInterface clientInterface;

	protected abstract void submit(String name, String address, int port);

	public ClientInterface getClientInterface() {
		return clientInterface;
	}
}
