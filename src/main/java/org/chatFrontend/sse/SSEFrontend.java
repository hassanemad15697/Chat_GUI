package org.chatFrontend.sse;

// SSEFrontend.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.chatFrontend.form.ChatWindow;
import org.json.JSONObject;

public class SSEFrontend {

    private String username;
    private String serverUrl = "http://localhost:8088/user/";
    private ChatWindow chatWindow;

    public SSEFrontend(String username, ChatWindow chatWindow) {
        this.username = username;
        this.chatWindow = chatWindow;
    }

    public void connect() {
        try {
            URL url = new URL(serverUrl + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(0); // No timeout, keep the connection indefinitely

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && line.startsWith("data:")) {
                    handleEvent(line.substring(5).trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvent(String event) {
        JSONObject jsonObject = new JSONObject(event);
        String eventType = jsonObject.getString("eventType");
        if ("newMessage".equals(eventType)) {
            String from = jsonObject.getString("from");
            String message = jsonObject.getString("message");
            chatWindow.addUserToList(from);
            chatWindow.appendToChatBox(from + ": " + message);
        }
    }
}