package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends Thread {

	private InetAddress ip;
	private int port;
	private DatagramSocket socket;

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
				System.out.println("Connected!");
			}
		}
	}

	public void run() {
		sendMessage("/c/~");

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
		try {
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ip, port);
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
