package org.chatFrontend.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Group {
    private String groupName;
    private Set<Message> message = new HashSet<>();

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Message> getMessage() {
        return message;
    }

    public void setMessage(Set<Message> message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getMessage().stream().map(msg -> msg.toString()).collect(Collectors.joining("\n"));
    }
}
