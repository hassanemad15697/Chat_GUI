package org.chatFrontend.form;
// ChatWindow.java

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chatFrontend.model.Group;
import org.chatFrontend.model.Message;
import org.chatFrontend.model.User;
import org.chatFrontend.singleton.ObjectMapperSingleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatWindow {
    private static JTextArea chatBox;
    private static DefaultListModel<String> groupListModel;
    private static DefaultListModel<String> userListModel;
    private static JList<String> groupList;
    private static final Map<String, Group> groups = new HashMap<>();
    private static final Map<String, User> users = new HashMap<>();
    private static JTextField messageField;
    ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();

    public ChatWindow() {
        setupGUI();
    }

    private static void chatPanelSetup(JPanel mainPanel) {
        JPanel chatPanel = new JPanel(new BorderLayout(10, 10));
        chatPanel.setBackground(new Color(240, 240, 240)); // Soft gray background

        chatBox = new JTextArea();
        chatBox.setText("Select a user or a group to start chatting");
        chatBox.setEditable(false);
        chatBox.setWrapStyleWord(true);
        chatBox.setLineWrap(true);
        chatBox.setBackground(Color.WHITE); // White chat box
        JScrollPane chatBoxScrollPane = new JScrollPane(chatBox);
        chatPanel.add(chatBoxScrollPane, BorderLayout.CENTER);

        JPanel sendPanel = new JPanel(new BorderLayout(10, 10));
        sendPanel.setBackground(new Color(240, 240, 240)); // Soft gray background
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(70, 130, 180)); // Modern blue
        sendButton.setForeground(Color.WHITE); // White text
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.addActionListener(e -> {
            // Append the written message to the chat box
            String message = messageField.getText();
            if (!message.isEmpty()) {
                Message newMessage = new Message(System.currentTimeMillis(), "You", "Group", message);
                chatBox.append("\n" + newMessage);
                messageField.setText("");
            }
        });
        sendPanel.add(messageField, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(sendPanel, BorderLayout.SOUTH);

        mainPanel.add(chatPanel, BorderLayout.CENTER);
    }

    private static JPopupMenu createUserPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem blockItem = new JMenuItem("Block");
        JMenuItem reportItem = new JMenuItem("Report");

        menu.add(blockItem);
        menu.add(reportItem);

        return menu;
    }

    private static JPopupMenu createGroupPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem joinGroupItem = new JMenuItem("Join Group");
        JMenuItem leaveGroupItem = new JMenuItem("Leave Group");
        JMenuItem reportGroupItem = new JMenuItem("Report Group");

        menu.add(joinGroupItem);
        menu.add(leaveGroupItem);
        menu.add(reportGroupItem);

        return menu;
    }

    private static void openPrivateChat(String selectedUser) {
        User user = users.get(selectedUser);
        Set<Message> messages = user.getMessage();
        StringBuilder chatContent = new StringBuilder();
        for (Message msg : messages) {
            chatContent.append(msg.toString()).append("\n");
        }
        chatBox.setText(chatContent.toString());
    }

    private void setupGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");
        groups.put(group1.getGroupName(), group1);
        groups.put(group2.getGroupName(), group2);

        // Sample messages
        Message msg1 = new Message(System.currentTimeMillis(), "User 1", "Group 1", "Sample message 1 from User 1");
        Message msg2 = new Message(System.currentTimeMillis(), "User 2", "Group 1", "Sample message 2 from User 2");
        group1.getMessage().add(msg1);
        group1.getMessage().add(msg2);

        Message msg3 = new Message(System.currentTimeMillis(), "User 3", "Group 2", "Sample message 1 from User 3");
        Message msg4 = new Message(System.currentTimeMillis(), "User 4", "Group 2", "Sample message 2 from User 4");
        group2.getMessage().add(msg3);
        group2.getMessage().add(msg4);

        JFrame frame = new JFrame("Chat Window");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240)); // Soft gray background

        // 1. Side panel for users
        userListModel = new DefaultListModel<>();
        updateUserListModel();

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
        userList.setComponentPopupMenu(createUserPopupMenu());
        userList.setBackground(new Color(245, 245, 245)); // Lighter gray
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        mainPanel.add(userScrollPane, BorderLayout.WEST);

        // 2. Side panel for groups
        groupListModel = new DefaultListModel<>();
        groupListModel.addElement(group1.getGroupName());
        groupListModel.addElement(group2.getGroupName());
        groupList = new JList<>(groupListModel);
        groupList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedGroup = groupList.getSelectedValue();
                    chatBox.setText(groups.get(selectedGroup).toString());
                }
            }
        });
        groupList.setComponentPopupMenu(createGroupPopupMenu());
        groupList.setBackground(new Color(245, 245, 245)); // Lighter gray
        JScrollPane groupScrollPane = new JScrollPane(groupList);
        groupScrollPane.setPreferredSize(new Dimension(150, 0));
        mainPanel.add(groupScrollPane, BorderLayout.EAST);

        // 3. Chat panel
        chatPanelSetup(mainPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void addUserToList(Set<String> usernames) {
        usernames.forEach(username -> {
            if (!users.containsKey(username)) {
                System.out.println(username+"added");
                users.put(username, new User(username));
            }
        });
        updateUserListModel();
    }

    public void removeUserFromList(Set<String> usernames) {
        usernames.forEach(username -> {
            if (users.containsKey(username)) {
                System.out.println(username+"removed");
                users.remove(username);
            }
        });
        updateUserListModel();
    }

    private void updateUserListModel() {
        userListModel.removeAllElements();
        users.forEach((s, user) -> {
            if(!userListModel.contains(s)){
                userListModel.addElement(s);
            }
        });
    }

    public void appendToChatBox(String message) {
        chatBox.append("\n" + message);
    }


}
