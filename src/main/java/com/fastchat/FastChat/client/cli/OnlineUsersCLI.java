package com.fastchat.FastChat.client.cli;

import com.fastchat.FastChat.client.OnlineUsersInterface;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineUsersCLI extends OnlineUsersInterface {

	private ArrayList<String> list;

	public OnlineUsersCLI() {
		init();
	}

	@Override
	public void init() {
		list = new ArrayList<>();
	}

	@Override
	public void update(String[] users) {
		list.clear();
		list.addAll(Arrays.asList(users));
	}

	@Override
	public void show() {
		System.out.println("=====================================");
		list.forEach(System.out::println);
		System.out.println("=====================================");
	}
}
