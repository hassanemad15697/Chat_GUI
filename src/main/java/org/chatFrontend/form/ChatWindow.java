package org.chatFrontend.form;
// ChatWindow.java
import org.chatFrontend.sse.SSEFrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatWindow {

    private static JTextArea chatBox;
    private static DefaultListModel<String> userListModel;

    public ChatWindow() {
        setupGUI();
    }

    private void setupGUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Chat Window");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240)); // Soft gray background

        // Side panel for users
        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedUser = userList.getSelectedValue();
                    openPrivateChat(selectedUser);
                }
            }
        });
        userList.setBackground(new Color(245, 245, 245)); // Lighter gray
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        mainPanel.add(userScrollPane, BorderLayout.WEST);

        // Chat panel
        chatPanelSetup(mainPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void chatPanelSetup(JPanel mainPanel) {
        JPanel chatPanel = new JPanel(new BorderLayout(10, 10));
        chatPanel.setBackground(new Color(240, 240, 240)); // Soft gray background

        chatBox = new JTextArea();
        chatBox.setText("Connecting to the server...");
        chatBox.setEditable(false);
        chatBox.setWrapStyleWord(true);
        chatBox.setLineWrap(true);
        chatBox.setBackground(Color.WHITE); // White chat box
        JScrollPane chatBoxScrollPane = new JScrollPane(chatBox);
        chatPanel.add(chatBoxScrollPane, BorderLayout.CENTER);

        JPanel sendPanel = new JPanel(new BorderLayout(10, 10));
        sendPanel.setBackground(new Color(240, 240, 240)); // Soft gray background
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(70, 130, 180)); // Modern blue
        sendButton.setForeground(Color.WHITE); // White text
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.addActionListener(e -> {
            // Append the written message to the chat box
            String message = messageField.getText();
            if (!message.isEmpty()) {
                chatBox.append("\nYou: " + message);
                messageField.setText("");
            }
        });
        sendPanel.add(messageField, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(sendPanel, BorderLayout.SOUTH);

        mainPanel.add(chatPanel, BorderLayout.CENTER);
    }

    private static void openPrivateChat(String selectedUser) {
        chatBox.setText("Private chat with " + selectedUser + "\n");
    }

    public void appendToChatBox(String message) {
        chatBox.append("\n" + message);
    }

    public void addUserToList(String user) {
        if (!userListModel.contains(user)) {
            userListModel.addElement(user);
        }
    }
}