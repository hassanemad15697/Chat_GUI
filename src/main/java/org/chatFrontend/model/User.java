package org.chatFrontend.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private String username;
    private Set<Message> message = new HashSet<>();

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Message> getMessage() {
        return message;
    }

    public void setMessage(Set<Message> message) {
        this.message = message;
    }
}
