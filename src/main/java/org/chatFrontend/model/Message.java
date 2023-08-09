package org.chatFrontend.model;


public class Message {

    private Long time;
    private String from;
    private String to;
    private String content;

    public Message(Long time, String from, String to, String content) {
        this.time = time;
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "["+this.getTime()+"]"+this.getFrom()+": "+this.getContent();
    }
}
