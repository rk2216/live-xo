package com.livegames.model;

import lombok.Data;

@Data
public class User {
    private MessageType type;
    private String content;
    private String userName;

    public enum MessageType {
        JOIN,
        LEAVE,
        INVALID,
        FULL,
        VALID
    }
}