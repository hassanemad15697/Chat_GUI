package org.chatFrontend.sse;

// SSEFrontend.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chatFrontend.form.ChatWindow;
import org.chatFrontend.singleton.ObjectMapperSingleton;
import org.json.JSONObject;

public class SSEFrontend {

    private String username;
    private String serverUrl = "http://localhost:8088/user/";
    private ChatWindow chatWindow;
    ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
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
                System.out.println(line+"\n");
                if (!line.isEmpty() && line.startsWith("data:")) {

                    handleEvent(line.substring(5).trim());
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvent(String event) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject(event);
        String eventType = jsonObject.getString("eventType");
        String message = jsonObject.getString("message");
        String from = jsonObject.getString("from");
        switch (eventType.toString()){
            case "usersList":
                System.out.println(message);
                UserGroupData data = objectMapper.readValue(message, UserGroupData.class);
                Set<String> userList = data.getUsers();
                Set<String> groupList = data.getGroups();
                System.out.println(userList);
                System.out.println(groupList);
                chatWindow.addUserToList(userList);
                chatWindow.addGroupToList(groupList);

                break;
            case "newMessage":
                chatWindow.appendToChatBox(from + ": " + message);
                break;
            case "userDelete":
                chatWindow.removeUserFromList(Set.of(from));
                break;
            case "userCreate":
                chatWindow.addUserToList(Set.of(from));
                break;
            default:
                break;
        }
    }

    static class UserGroupData {
        private Set<String> users;
        private Set<String> groups;

        public Set<String> getUsers() {
            return users;
        }

        public void setUsers(Set<String> users) {
            this.users = users;
        }

        public Set<String> getGroups() {
            return groups;
        }

        public void setGroups(Set<String> groups) {
            this.groups = groups;
        }
    }
}

