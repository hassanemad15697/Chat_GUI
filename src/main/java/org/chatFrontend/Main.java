package org.chatFrontend;

import org.chatFrontend.form.ChatWindow;
import org.chatFrontend.sse.SSEFrontend;

// Main.java
public class Main {

    public static void main(String[] args) {
        ChatWindow chatWindow = new ChatWindow();
        SSEFrontend frontend = new SSEFrontend("sampleUsername", chatWindow);
        frontend.connect();
    }
}