package com.fastchat.FastChat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 5015815262080715846L;
	
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	
	private Thread run, listen;
	private Client client;
	
	public ClientWindow(String name, String address, int port) {
		client = new Client(name, port);
		client.setRunning(true);
		createWindow();
		console("Attempting to conect to " + address + ":" + port + "...");
		boolean connected = client.openConnection(address);
		if (!connected) {
			System.err.println("Connection failed!");
			console("Connection failed!");
			txtMessage.setEditable(false);
		} else {
			run = new Thread(this, "Run");
			run.start();
		}
		client.send(("/c/" + name));
	}
	
	private void console(String message) {
		history.setCaretPosition(history.getDocument().getLength());
		history.append(message + "\r\n");
	}
	
	private void send(String message) {
		if (message.equals("")) return;
		message = client.getName() + ": " + message;
		txtMessage.setText("");
		client.send("/m/" + message);
	}
	
	public void run() {
		listen();
	}
	
	private void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while (client.isRunning()) {
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.substring(3)));
						console("Connection is setted up successfully... ID is " + client.getID());
					} else if (message.startsWith("/m/")) {
						console(message.substring(3));
					} else if (message.startsWith("/i/")) {
						client.send("/i/" + client.getID());
					}else if (message.startsWith("/d/")) {
						message = message.substring(3);
						if (message.equals("0")) {
							client.setKicked(true);
							client.disconnect(client.getID());
						} else {
							client.setBanned(true);
							client.disconnect(client.getID());
						}
					} else {
						console("Server: " + message);
					}
				}
			}
		};
		listen.start();
	}
	
	private void createWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 5, WIDTH - 30, 20, 0 };
		gbl_contentPane.rowHeights = new int[] { 5, HEIGHT - 30, 20, 5 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		gbc_scrollPane.gridwidth = 2;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		history = new JTextArea();
		history.setEditable(false);
		caret = (DefaultCaret) history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(history);
		
		txtMessage = new JTextField();
		txtMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText());
			}
		});
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		contentPane.add(txtMessage, gbc_textField);
		txtMessage.setColumns(10);
		txtMessage.requestFocusInWindow();
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText());
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				client.disconnect(client.getID());
			}
		});
		setTitle("FastChat Client. User: " + client.getName());
	}
}
