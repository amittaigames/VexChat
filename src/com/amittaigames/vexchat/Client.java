package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends Thread {

	private InetAddress ip;
	private int port;
	private DatagramSocket socket;

	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.ip = InetAddress.getByName("localhost");
			client.port = 12098;
			client.socket = new DatagramSocket();
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
