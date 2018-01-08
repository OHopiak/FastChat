package com.fastchat.FastChat.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

	private static final long serialVersionUID = -6824082102990197571L;

	private static Login currentLogin;
	private static ClientWindow clientWindow;

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;

	private Login() {
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(205, 295);
		setLocationRelativeTo(null);

		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtName = new JTextField();
		txtName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtAddress.requestFocusInWindow();
			}
		});
		txtName.setToolTipText("Name");
		txtName.setBounds(25, 30, 140, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		txtName.requestFocusInWindow(); // requesting focus

		txtAddress = new JTextField();
		txtAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPort.requestFocusInWindow();
			}
		});
		txtAddress.setToolTipText("Address");
		txtAddress.setColumns(10);
		txtAddress.setBounds(25, 90, 140, 20);
		contentPane.add(txtAddress);

		txtPort = new JTextField();
		txtPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submit();
			}
		});
		txtPort.setToolTipText("Port");
		txtPort.setColumns(10);
		txtPort.setBounds(25, 154, 140, 20);
		contentPane.add(txtPort);

		final JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submit();
			}
		});
		btnSubmit.setBounds(53, 216, 89, 23);
		contentPane.add(btnSubmit);

		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(72, 11, 46, 14);
		contentPane.add(lblName);

		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setBounds(72, 72, 46, 14);
		contentPane.add(lblAddress);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setBounds(72, 135, 46, 14);
		contentPane.add(lblPort);
		txtName.setText("Orest");
		txtAddress.setText("localhost");
		txtPort.setText("1234");
	}

	private void submit() {
		String name = txtName.getText();
		String address = txtAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		clientWindow = new ClientWindow(name, address, port);
		this.setVisible(false);
	}

	public void reset() {
		this.setVisible(true);
		txtAddress.setText("");
		txtPort.setText("");
		txtAddress.requestFocusInWindow();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(Login::getCurrentLogin);
	}

	public static Login getCurrentLogin() {
		if (currentLogin == null)
			currentLogin = new Login();
		return currentLogin;
	}

	public static void setCurrentLogin(Login login) {
		currentLogin = login;
	}

	public static ClientWindow getClientWindow() {
		return clientWindow;
	}

	public static void setClientWindow(ClientWindow clientWindow) {
		Login.clientWindow = clientWindow;
	}

}
