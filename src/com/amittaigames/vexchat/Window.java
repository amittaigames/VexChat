package com.amittaigames.vexchat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window {

	private static JFrame frame;
	private static JTextArea log;

	private static Client client;

	public static void init(Client client) {
		Window.client = client;

		frame = new JFrame("Chatting on " + client.getIP().getHostAddress() + ":" + client.getPort());
		frame.setSize(800, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBackground(new Color(0, 210, 108));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);
		panel.setBackground(new Color(0, 210, 108));

		log = new JTextArea();
		log.setBounds(10, 10, frame.getWidth() - 25, frame.getHeight() - 75);
		log.setBorder(new EmptyBorder(5, 5, 5, 5));
		log.setEditable(false);
		log.setFont(new Font("Courier", Font.PLAIN, 14));
		log.append("Vex Chat v0.1\n");
		log.append("Connecting...\n");
		panel.add(log);

		JTextField msg = new JTextField();
		msg.setBounds(7, 10 + frame.getHeight() - 70, frame.getWidth() - 110, 30);
		msg.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// Nothing
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					client.sendMessage(msg.getText());
					msg.setText("");
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// Nothing
			}
		});
		panel.add(msg);

		JButton send = new JButton("Send");
		send.setBounds(frame.getWidth() - 105, 10 + frame.getHeight() - 70, 95, 30);
		send.addActionListener(e -> {
			client.sendMessage(msg.getText());
			msg.setText("");
		});
		panel.add(send);

		frame.setContentPane(panel);
		frame.setVisible(true);

		msg.requestFocus();
	}

	public static void log(String msg) {
		log.append(msg + "\n");
	}

}