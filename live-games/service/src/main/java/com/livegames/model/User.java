package com.livegames.model;

public class User {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        JOIN,
        LEAVE,
        INVALID,
        FULL,
        VALID
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}