package com.amittaigames.vexchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends Thread {

	private InetAddress ip;
	private int port;
	private DatagramSocket socket;
	private String name;

	public static final int VERSION = 6;
	public static final String S_VERSION = "0.6";
	private static final int SEND_MSG = 1;
	private static final int SEND_USR = 2;
	private static boolean connected = false;

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
		//
		//	/c/
		//
		if (args[0].equals("/c/")) {
			if (args[1].equals("OK")) {
				getUsernameForServer();
			}
			else if (args[1].equals("VERSION")) {
				Window.log("Your version (" + S_VERSION + ") is not compatible with the server (" + args[2] + ")");
			}
			else {
				Window.log("Unable to connect!");
			}
		}

		//
		//	/cu/
		//
		else if (args[0].equals("/cu/")) {
			if (args[1].equals("OK")) {
				Window.log("Connection successful!");
				connected = true;
				return;
			} else if (args[1].equals("USER_EXIST")) {
				Window.log("Username already exists");
			} else if (args[1].equals("BAD_NAME")) {
				Window.log("Invalid username (no spaces or symbols!)");
			}
			getUsernameForServer();
		}

		//
		//	/s/
		//
		else if (args[0].equals("/s/")) {
			Window.log("[SERVER] " + args[1]);
		}

		//
		//	/m/
		//
		else if (args[0].equals("/m/")) {
			Window.log("[" + args[1] + "] " + args[2]);
		}

		//
		//	/i/
		//
		else if (args[0].equals("/i/")) {
			sendPacket("/i/~" + name + "~" + SystemData.toPacketData(new SystemData()));
		}
	}

	public void run() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					if (connected)
						sendPacket("/x/~" + name);
				} catch (Exception e) {
					// Do nothing because it should work if a connetion exists
				}
			}
		});

		Window.init(this);
		sendPacket("/c/~" + VERSION);

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
		if (msg.isEmpty()) {
			return;
		}

		if (msg.startsWith("/")) {
			handleCommand(msg.replace("/", ""));
		} else {
			if (sendMode == SEND_MSG) {
				if (connected)
					sendPacket("/m/~" + name + "~" + msg);
			} else if (sendMode == SEND_USR) {
				if (msg.contains("~")) {
					Window.log("Invalid username (no spaces or symbols!)");
					return;
				}
				sendPacket("/cu/~" + msg);
				this.name = msg;
				sendMode = SEND_MSG;
			}
		}
	}

	public void handleCommand(String cmd) {
		//
		//	exit
		//
		if (cmd.equals("exit")) {
			System.exit(0);
		}

		//
		//	online
		//
		else if (cmd.equals("online")) {
			sendPacket("/cmd/~ONLINE");
		}

		//
		//	afk
		//
		else if (cmd.equals("afk")) {
			sendPacket("/cmd/~AFK~" + name);
		}
		
		//
		//	list
		//
		else if (cmd.equals("list")) {
			sendPacket("/cmd/~LS");
		}

		//
		//	Invalid
		//
		else {
			Window.log("[CMD] Invalid command");
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
