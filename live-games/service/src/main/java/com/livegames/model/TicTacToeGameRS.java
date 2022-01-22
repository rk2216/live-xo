package com.livegames.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class TicTacToeGameRS {
    List<Character> board;
    Map<String, Character> players;
    Character currentTurn;
}