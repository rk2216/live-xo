package com.livegames.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Room {
    private GameType gameType;
    private HashMap<String, User> userMap;
    private String roomId;

}
