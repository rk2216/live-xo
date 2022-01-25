package com.livegames.model;

import lombok.Data;

@Data
public class CreateRoomRQ {
    private User user;
    private GameType gameType;
}
