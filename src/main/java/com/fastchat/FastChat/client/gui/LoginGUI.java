package com.fastchat.FastChat.client.gui;

import com.fastchat.FastChat.client.LoginInterface;
import com.fastchat.FastChat.util.Localization;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class LoginGUI extends LoginInterface {

	private static final String title = "Login";

	private JFrame frame;
	private JPanel loginPanel;
	private JLabel lblName;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JButton submitButton;
	private JLabel lblAddress;
	private JLabel lblPort;

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	public LoginGUI() {
		init();
	}

	@Override
	protected void submit(String name, String address, int port) {
		clientInterface = ClientGUI.getInstance(name, address, port);
		clientInterface.init();
	}

	public void reset() {
		frame.setVisible(true);
//		txtAddress.setText("localhost");
//		txtPort.setText("1234");
		txtAddress.requestFocusInWindow();
	}

	private void init() {
		frame = new JFrame(title);
		frame.setContentPane(loginPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(205, 295);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		txtName.addActionListener(e -> txtAddress.requestFocusInWindow());
		txtAddress.addActionListener(e -> txtPort.requestFocusInWindow());
		ActionListener submitListener = e -> {
			String name = txtName.getText();
			String address = txtAddress.getText();
			int port = Integer.parseInt(txtPort.getText());
			submit(name, address, port);
			frame.setVisible(false);
		};
		txtPort.addActionListener(submitListener);
		submitButton.addActionListener(submitListener);

		lblName.setText(Localization.get("login_lbl_name") + ":");
		lblAddress.setText(Localization.get("login_lbl_address") + ":");
		lblPort.setText(Localization.get("login_lbl_port") + ":");
		submitButton.setText(Localization.get("login_submit"));
		submitButton.setMnemonic(Localization.get("login_submit").charAt(0));

		txtName.setText("Anon");
		txtAddress.setText("localhost");
		txtPort.setText("1234");

		// Set up window icon
		URL iconUrl = getClass().getClassLoader().getResource("FastChatLogo_filled.png");
		if (iconUrl != null) {
			ImageIcon icon;
			icon = new ImageIcon(iconUrl);
			frame.setIconImage(icon.getImage());
		}

		frame.setVisible(true);
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		loginPanel = new JPanel();
		loginPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
		loginPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		lblName = new JLabel();
		lblName.setText("Name:");
		CellConstraints cc = new CellConstraints();
		loginPanel.add(lblName, cc.xy(1, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
		txtName = new JTextField();
		txtName.setColumns(15);
		txtName.setText("");
		loginPanel.add(txtName, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
		lblAddress = new JLabel();
		lblAddress.setText("Address:");
		loginPanel.add(lblAddress, cc.xy(1, 9, CellConstraints.CENTER, CellConstraints.DEFAULT));
		txtAddress = new JTextField();
		txtAddress.setColumns(15);
		loginPanel.add(txtAddress, cc.xy(1, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
		lblPort = new JLabel();
		lblPort.setText("Port:");
		loginPanel.add(lblPort, cc.xy(1, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
		txtPort = new JTextField();
		txtPort.setColumns(15);
		loginPanel.add(txtPort, cc.xy(1, 17, CellConstraints.CENTER, CellConstraints.DEFAULT));
		submitButton = new JButton();
		submitButton.setText("Submit");
		loginPanel.add(submitButton, cc.xy(1, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
		final JSeparator separator1 = new JSeparator();
		separator1.setEnabled(true);
		separator1.setVisible(false);
		loginPanel.add(separator1, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		final JSeparator separator2 = new JSeparator();
		separator2.setVisible(false);
		loginPanel.add(separator2, cc.xy(1, 7, CellConstraints.FILL, CellConstraints.FILL));
		final JSeparator separator3 = new JSeparator();
		separator3.setVisible(false);
		loginPanel.add(separator3, cc.xy(1, 13, CellConstraints.FILL, CellConstraints.FILL));
		final JSeparator separator4 = new JSeparator();
		separator4.setVisible(false);
		loginPanel.add(separator4, cc.xy(1, 19, CellConstraints.FILL, CellConstraints.FILL));
		final JSeparator separator5 = new JSeparator();
		separator5.setVisible(false);
		loginPanel.add(separator5, cc.xy(1, 23, CellConstraints.FILL, CellConstraints.FILL));
		lblName.setLabelFor(txtName);
		lblAddress.setLabelFor(txtAddress);
		lblPort.setLabelFor(txtPort);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return loginPanel;
	}
}
