package com.livegames.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class Room {
    private GameType gameType;
    private HashMap<String, User> userMap;
    private String roomId;
    private User host;
}
