package com.livegames.model;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TicTacToeGameRS extends Room {
    List<Character> board;
    Map<String, Character> players;
    Character currentTurn;
    Character winner;
}