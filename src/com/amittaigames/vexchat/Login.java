package com.amittaigames.vexchat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

	private static JFrame frame;

	public static void init() {
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(10, 1));

		JLabel lblHost = new JLabel("Server IP:");
		panel.add(lblHost);

		JTextField txtHost = new JTextField();
		panel.add(txtHost);

		JLabel lblPort = new JLabel("Port:");
		panel.add(lblPort);

		JTextField txtPort = new JTextField();
		panel.add(txtPort);

		JButton conn = new JButton("Connect");
		conn.addActionListener(e -> {
			Client client = new Client(
					txtHost.getText(),
					Integer.parseInt(txtPort.getText())
			);
			client.start();
		});
		panel.add(conn);

		frame = new JFrame("Connect to a Server");
		frame.setSize(350, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

}
