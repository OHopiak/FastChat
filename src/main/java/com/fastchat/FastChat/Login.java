package com.fastchat.FastChat;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class Login extends JFrame {

	private static final long serialVersionUID = -6824082102990197571L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;

	public Login() {
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 360);
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
		txtName.setBounds(77, 44, 140, 20);
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
		txtAddress.setBounds(77, 126, 140, 20);
		contentPane.add(txtAddress);

		txtPort = new JTextField();
		txtPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					submit();
			}
		});
		txtPort.setToolTipText("Port");
		txtPort.setColumns(10);
		txtPort.setBounds(77, 199, 140, 20);
		contentPane.add(txtPort);

		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(124, 25, 46, 14);
		contentPane.add(lblName);

		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setBounds(124, 108, 46, 14);
		contentPane.add(lblAddress);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setBounds(124, 180, 46, 14);
		contentPane.add(lblPort);

		final JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					submit();
			}
		});
		btnSubmit.setBounds(102, 271, 89, 23);
		contentPane.add(btnSubmit);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { txtName, txtAddress, txtPort, btnSubmit, lblName, lblAddress, lblPort }));
	}

	private void submit() {
		String name = txtName.getText();
		String address = txtAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		new Client(name, address, port);
		this.setVisible(false);
	}
	
	public void reset() {
		this.setVisible(true);
		txtAddress.setText("");
		txtPort.setText("");
		txtAddress.requestFocusInWindow();
	}
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Login();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
