package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends Thread {

	private int port;
	private DatagramSocket socket;

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

}