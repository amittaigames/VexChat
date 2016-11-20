package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

	private int port;
	private DatagramSocket socket;

	private List<User> users = new ArrayList<>();

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.port = 12098;
			server.socket = new DatagramSocket(server.port);
			server.start();

			System.out.println("Server started on port " + server.port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePacket(String[] args, DatagramPacket packet) {
		if (args[0].equals("/c/")) {
			sendPacket("/c/~OK", packet.getAddress(), packet.getPort());
			System.out.println("Connection from " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
		} else if (args[0].equals("/cu/")) {
			if (userExists(args[1])) {
				sendPacket("/cu/~USER_EXIST", packet.getAddress(), packet.getPort());
			} else {
				sendPacket("/cu/~OK", packet.getAddress(), packet.getPort());
				sendPacket("/s/~Welcome to the server " + args[1] + "!", packet.getAddress(), packet.getPort());
				sendToAll("/s/~" + args[1] + " joined the chat room");
				users.add(new User(packet.getAddress(), packet.getPort(), args[1]));
			}
		} else if (args[0].equals("/m/")) {
			sendToAll("/m/~" + args[1] + "~" + args[2]);
		}
	}

	public void run() {
		while (true) {
			try {
				byte[] buffer = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String msg = new String(buffer).trim();
				String[] args = msg.split("~");
				handlePacket(args, packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendPacket(String msg, InetAddress ip, int port) {
		try {
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ip, port);
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToAll(String msg) {
		for (User u : users) {
			sendPacket(msg, u.getIP(), u.getPort());
		}
	}

	private boolean userExists(String name) {
		for (User u : users) {
			if (u.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}

}