package com.fastchat.FastChat.beans;

public class UserBean {
	private String name;
	private int ID;

	public UserBean() {
	}

	UserBean(String name, int ID) {
		this.name = name;
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
}
