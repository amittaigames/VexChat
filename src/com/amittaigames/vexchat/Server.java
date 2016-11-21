package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
		//
		//	/c/
		//
		if (args[0].equals("/c/")) {
			sendPacket("/c/~OK", packet.getAddress(), packet.getPort());
			System.out.println(time() + "Connection from " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
		}

		//
		//	/cu/
		//
		else if (args[0].equals("/cu/")) {
			if (userExists(args[1])) {
				sendPacket("/cu/~USER_EXIST", packet.getAddress(), packet.getPort());
			} else {
				sendPacket("/cu/~OK", packet.getAddress(), packet.getPort());
				sendPacket("/s/~Welcome to the server " + args[1] + "!", packet.getAddress(), packet.getPort());
				sendToAll("/s/~" + args[1] + " joined the chat room");
				users.add(new User(packet.getAddress(), packet.getPort(), args[1]));
				System.out.println(time() + "User '" + args[1] + "' joined the chat room");
			}
		}

		//
		//	/m/
		//
		else if (args[0].equals("/m/")) {
			sendToAll("/m/~" + args[1] + "~" + args[2]);
			System.out.println(time() + "[" + args[1] + "] " + args[2]);
		}

		//
		//	/x/
		//
		else if (args[0].equals("/x/")) {
			int id = getIDByUsername(args[1]);
			if (id != -1) {
				users.remove(id);
				sendToAll("/s/~" + args[1] + " left the chat room");
				System.out.println(time() + args[1] + " disconnected");
			} else {
				System.err.println(time() + "UDNE (EXIT): " + args[1]);
			}
		}

		//
		//	/cmd/
		//
		else if (args[0].equals("/cmd/")) {
			if (args[1].equals("ONLINE")) {
				int size = users.size();
				sendPacket("/s/~" + size + " user(s) online", packet.getAddress(), packet.getPort());
			} else if (args[1].equals("AFK")) {
				int id = getIDByUsername(args[2]);
				if (id != -1) {
					sendToAll("/s/~" + users.get(id).getUsername() + " is AFK");
					System.out.println(time() + users.get(id).getUsername() + " is AFK");
				} else {
					System.err.println(time() + "UNDE (AFK): " + args[2]);
				}
			}
		}

		//
		//	Unknown
		//
		else {
			StringBuilder sb = new StringBuilder();
			for (String s : args) {
				sb.append(s).append("~");
			}
			String upacket = sb.toString();
			System.err.println(time() + "Invalid packet from " + packet.getAddress().getHostAddress() + ":" +
					packet.getPort() + " - '" + upacket);
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

	private String time() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		String date = sdf.format(new Date());
		return "[" + date + "] ";
	}

	private boolean userExists(String name) {
		for (User u : users) {
			if (u.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private int getIDByUsername(String name) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(name)) {
				return i;
			}
		}
		return -1;
	}

}