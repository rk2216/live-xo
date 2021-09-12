package com.livegames.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Room {
    private GameType gameType;
    private List<User> userList;
    private String roomId;

}
