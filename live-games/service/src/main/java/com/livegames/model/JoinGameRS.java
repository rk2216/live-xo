package com.livegames.model;


import lombok.Data;

@Data
public class JoinGameRS {
    private User host;
    private User user;
    private String[] members;
}
