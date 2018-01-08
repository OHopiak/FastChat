package com.fastchat.FastChat.client;

import com.fastchat.FastChat.util.Localization;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The basic GUI class,
 * has a generated part that can't be edited
 */
public class ClientGUI extends ClientInterface {
	private final static int WIDTH = 400;
	private final static int HEIGHT = 600;
	private final static String TITLE = Localization.get("window_title");
	private static ClientGUI clientGUI;
	private JFrame frame;
	private JTextField messagePrompt;
	private JButton sendButton;
	private JScrollPane messagePanel;
	private JPanel clientPanel;
	private JTextArea history;

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 *
	 */
	private ClientGUI() {
		init();
		frame.setVisible(true);
	}

	/**
	 * @return the singleton instance
	 */
	public static ClientGUI getInstance() {
		if (clientGUI == null)
			clientGUI = new ClientGUI();
		return clientGUI;
	}

	/**
	 * @return generated MenuBar
	 */
	private static JMenuBar generateMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu(Localization.get("menu_bar_menu"));
		menuBar.add(menu);
		JMenuItem onlineUsers = new JMenuItem(Localization.get("menu_bar_online_users"));
		onlineUsers.addActionListener(event -> {
//				client.send("/u/");
//				users.updateList(userArr);
//				users.setVisible(true);
		});
		menu.add(onlineUsers);

		JMenuItem settings = new JMenuItem(Localization.get("menu_bar_settings"));
		settings.addActionListener(event -> {

		});
		menu.add(settings);

		return menuBar;
	}

	/**
	 *
	 */
	private void init() {
		ActionListener messageListener = actionEvent -> send(messagePrompt.getText());
		messagePrompt.addActionListener(messageListener);
		sendButton.addActionListener(messageListener);
		sendButton.setText(Localization.get("send_btn_text"));
		((DefaultCaret) history.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setJMenuBar(generateMenuBar());

		frame.setContentPane(this.clientPanel);
		frame.pack();

		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
	}

	@Override
	protected void send(String message) {
		if (message.equals("")) return;
//		message = client.getName() + ": " + message;
		messagePrompt.setText("");
//		client.send("/m/" + message);
		console(message);
	}

	@Override
	void console(String message) {
		history.setCaretPosition(history.getDocument().getLength());
		history.append(message + "\r\n");
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		clientPanel = new JPanel();
		clientPanel.setLayout(new GridBagLayout());
		clientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6), null));
		messagePanel = new JScrollPane();
		messagePanel.setBackground(new Color(-1));
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 10.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 4, 0);
		clientPanel.add(messagePanel, gbc);
		history = new JTextArea();
		history.setEditable(false);
		messagePanel.setViewportView(history);
		messagePrompt = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		clientPanel.add(messagePrompt, gbc);
		sendButton = new JButton();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 3, 0, 0);
		clientPanel.add(sendButton, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return clientPanel;
	}
}
