package com.fastchat.FastChat.client;

import com.fastchat.FastChat.util.Localization;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OnlineUsers extends JFrame {

	private static final long serialVersionUID = -3779778679932068169L;
	private JPanel contentPane;
	private JList<String> list;

	OnlineUsers() {
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(278, 409);
		setLocationRelativeTo(null);
		setTitle(Localization.get("menu_bar_online_users"));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		list = new JList<>();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		contentPane.add(list, gbc_list);
	}

	public void updateList(String[] users) {
		list.setListData(users);
	}
}
