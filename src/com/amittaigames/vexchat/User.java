package com.amittaigames.vexchat;

import java.net.InetAddress;

public class User {

	private InetAddress ip;
	private int port;
	private String username;

	public User(InetAddress ip, int port, String username) {
		this.ip = ip;
		this.port = port;
		this.username = username;
	}

	public InetAddress getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

}
