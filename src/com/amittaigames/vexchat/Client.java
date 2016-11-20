package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends Thread {

	private InetAddress ip;
	private int port;
	private DatagramSocket socket;
	private String name;

	private static final int SEND_MSG = 1;
	private static final int SEND_USR = 2;

	private int sendMode = SEND_MSG;

	public Client(String ip, int port) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.port = port;
			this.socket = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Login.init();
	}

	private void handlePacket(String[] args) {
		if (args[0].equals("/c/")) {
			if (args[1].equals("OK")) {
				getUsernameForServer();
			} else {
				Window.log("Unable to connect!");
			}
		} else if (args[0].equals("/cu/")) {
			if (args[1].equals("OK")) {
				Window.log("Connection successful!");
				return;
			} else if (args[1].equals("USER_EXIST")) {
				Window.log("Username already exists");
			}
			getUsernameForServer();
		} else if (args[0].equals("/s/")) {
			Window.log("[SERVER] " + args[1]);
		} else if (args[0].equals("/m/")) {
			Window.log("[" + args[1] + "] " + args[2]);
		}
	}

	public void run() {
		Window.init(this);
		sendPacket("/c/~");

		while (true) {
			try {
				byte[] buffer = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String msg = new String(buffer).trim();
				String[] args = msg.split("~");
				handlePacket(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String msg) {
		if (sendMode == SEND_MSG) {
			sendPacket("/m/~" + name + "~" + msg);
		} else if (sendMode == SEND_USR) {
			sendPacket("/cu/~" + msg);
			this.name = msg;
			sendMode = SEND_MSG;
		}
	}

	public void sendPacket(String msg) {
		try {
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ip, port);
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getUsernameForServer() {
		sendMode = SEND_USR;
		Window.log("Please enter a username");
	}

	public InetAddress getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

}
