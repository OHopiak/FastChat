package com.fastchat.FastChat;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class Client extends JFrame {
	
	private static final long serialVersionUID = 5015815262080715846L;
	private JPanel contentPane;
	
	private String name;
	private String address;
	private int port;
	
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	private boolean connected;
	private DatagramSocket socket;
	private InetAddress ip;
	private Thread send;
	
	public Client(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
		createWindow();
		console("Attempting to conect to " + address + ":" + port);
		connected = openConnection(this.address, port);
		if (!connected) {
			// System.err.println("Connection failed");
			console("Connection failed");
			txtMessage.setEditable(false);
		} else {
			console("Connected successfully\r\n");
		}
		String connection = name + " connected from " + address + ":"  + port;
		send(connection.getBytes());
		
	}
	
	private boolean openConnection(String address, int port) {
		try {
			socket = new DatagramSocket(port);
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
	}
	
	private void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	private void console(String message) {
		history.setCaretPosition(history.getDocument().getLength());
		history.append(message + "\r\n");
	}
	
	private void send(String message) {
		if (message.equals("")) return;
		message = name + ": " + message;
		console(message);
		send(message.getBytes());
		txtMessage.setText("");
	}
	
	private void createWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		contentPane.setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[] { scrollPane, btnSend, history, txtMessage }));
	}
}
