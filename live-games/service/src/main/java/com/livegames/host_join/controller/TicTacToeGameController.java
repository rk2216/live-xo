package com.livegames.host_join.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.livegames.model.TicTacToeGameRQ;
import com.livegames.model.TicTacToeGameRS;
import com.livegames.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import lombok.Data;

@Data
@Controller
public class TicTacToeGameController {
    private final SimpMessageSendingOperations messagingTemplate;

    Map<String, TicTacToeGameRS> gameMap = new HashMap<>();

    @Autowired
    public TicTacToeGameController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/ticTacToe/{roomId}")
    public void updateBoard(@Payload TicTacToeGameRQ ticTacToeGameRQ, @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor) {
        TicTacToeGameRS ticTacToeGameRS = gameMap.get(roomId);
        ticTacToeGameRS.getBoard().set(ticTacToeGameRQ.getIndex(), ticTacToeGameRQ.getCurrentTurn());
        ticTacToeGameRS.setCurrentTurn(ticTacToeGameRS.getCurrentTurn().equals('X') ? 'O' : 'X');
        messagingTemplate.convertAndSend("/topic/public/" + roomId, ticTacToeGameRS);
    }

    @MessageMapping("/ticTacToe/reset/{roomId}")
    public void resetBoard(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        TicTacToeGameRS ticTacToeGameRS = gameMap.get(roomId);
        ticTacToeGameRS.setBoard(new ArrayList<>(Collections.nCopies(9, null)));
        ticTacToeGameRS.setCurrentTurn('X');
        messagingTemplate.convertAndSend("/topic/public/" + roomId, ticTacToeGameRS);
    }

    @MessageMapping("/ticTacToe/create/{roomId}")
    public void createBoard(@Payload User user, @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor) {
        TicTacToeGameRS ticTacToeGameRS;
        if (gameMap.containsKey(roomId)) {
            ticTacToeGameRS = gameMap.get(roomId);
            Map<String, Character> players = ticTacToeGameRS.getPlayers();
            players.put(user.getUserName(), players.entrySet().iterator().next().getValue().equals('X') ? 'O' : 'X');
            ticTacToeGameRS.setPlayers(players);
            gameMap.put(roomId, ticTacToeGameRS);
        } else {
            ticTacToeGameRS = new TicTacToeGameRS();
            Map<String, Character> players = new HashMap<>();
            players.put(user.getUserName(), 'X');
            ticTacToeGameRS.setBoard(new ArrayList<>(Collections.nCopies(9, null)));
            ticTacToeGameRS.setPlayers(players);
            ticTacToeGameRS.setCurrentTurn('X');
            gameMap.put(roomId, ticTacToeGameRS);
        }
        messagingTemplate.convertAndSend("/topic/public/" + roomId, ticTacToeGameRS);
    }
}
