package com.livegames.host_join.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.livegames.model.Room;
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

    @Resource(name = "RoomsMap")
    Map<String, Room> roomsMap;

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
        Character winner = findWinner(ticTacToeGameRS.getBoard());
        for (Map.Entry<String, Character> entry : ticTacToeGameRS.getPlayers().entrySet()) {
            if (entry.getValue().equals(winner)) {
                Room room = roomsMap.get(roomId);
                User user = room.getUserMap().get(entry.getKey());
                user.setScore(user.getScore()+1);
                ticTacToeGameRS.setUserMap(room.getUserMap());
            }
        }
        ticTacToeGameRS.setWinner(winner);
        messagingTemplate.convertAndSend("/topic/public/" + roomId, ticTacToeGameRS);
    }

    @MessageMapping("/ticTacToe/reset/{roomId}")
    public void resetBoard(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        TicTacToeGameRS ticTacToeGameRS = gameMap.get(roomId);
        ticTacToeGameRS.setBoard(new ArrayList<>(Collections.nCopies(9, null)));
        ticTacToeGameRS.setCurrentTurn('X');
        ticTacToeGameRS.setWinner(null);
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
            ticTacToeGameRS.setWinner(null);
            ticTacToeGameRS.setUserMap(roomsMap.get(roomId).getUserMap());
            gameMap.put(roomId, ticTacToeGameRS);
        }
        messagingTemplate.convertAndSend("/topic/public/" + roomId, ticTacToeGameRS);
    }

    Character findWinner(List<Character> board) {
        //Column winner
        if((board.get(0) != null && board.get(0).equals('X') 
        && board.get(3) != null && board.get(3).equals('X') 
        && board.get(6) != null && board.get(6).equals('X')) || 
        (board.get(1) != null && board.get(1).equals('X') 
        && board.get(4) != null && board.get(4).equals('X') 
        && board.get(7) != null && board.get(7).equals('X')) || 
        (board.get(2) != null && board.get(2).equals('X') 
        && board.get(5) != null && board.get(5).equals('X') 
        && board.get(8) != null && board.get(8).equals('X'))) {
            return 'X';
        }
        if((board.get(0) != null && board.get(0).equals('O') 
        && board.get(3) != null && board.get(3).equals('O') 
        && board.get(6) != null && board.get(6).equals('O')) || 
        (board.get(1) != null && board.get(1).equals('O') 
        && board.get(4) != null && board.get(4).equals('O') 
        && board.get(7) != null && board.get(7).equals('O')) || 
        (board.get(2) != null && board.get(2).equals('O') 
        && board.get(5) != null && board.get(5).equals('O') 
        && board.get(8) != null && board.get(8).equals('O'))) {
            return 'O';
        }
        //Row winner
        if((board.get(0) != null && board.get(0).equals('X') 
        && board.get(1) != null && board.get(1).equals('X') 
        && board.get(2) != null && board.get(2).equals('X')) || 
        (board.get(3) != null && board.get(3).equals('X') 
        && board.get(4) != null && board.get(4).equals('X') 
        && board.get(5) != null && board.get(5).equals('X')) || 
        (board.get(6) != null && board.get(6).equals('X') 
        && board.get(7) != null && board.get(7).equals('X') 
        && board.get(8) != null && board.get(8).equals('X'))) {
            return 'X';
        }
        if((board.get(0) != null && board.get(0).equals('O') 
        && board.get(1) != null && board.get(1).equals('O') 
        && board.get(2) != null && board.get(2).equals('O')) || 
        (board.get(3) != null && board.get(3).equals('O') 
        && board.get(4) != null && board.get(4).equals('O') 
        && board.get(5) != null && board.get(5).equals('O')) || 
        (board.get(6) != null && board.get(6).equals('O') 
        && board.get(7) != null && board.get(7).equals('O') 
        && board.get(8) != null && board.get(8).equals('O'))) {
            return 'O';
        }
        //Diagonal Winner
        if((board.get(0) != null && board.get(0).equals('X') 
        && board.get(4) != null && board.get(4).equals('X') 
        && board.get(8) != null && board.get(8).equals('X')) || 
        (board.get(2) != null && board.get(2).equals('X') 
        && board.get(4) != null && board.get(4).equals('X') 
        && board.get(6) != null && board.get(6).equals('X'))) {
            return 'X';
        }
        if((board.get(0) != null && board.get(0).equals('O') 
        && board.get(4) != null && board.get(4).equals('O') 
        && board.get(8) != null && board.get(8).equals('O')) || 
        (board.get(2) != null && board.get(2).equals('O') 
        && board.get(4) != null && board.get(4).equals('O') 
        && board.get(6) != null && board.get(6).equals('O'))) {
            return 'O';
        }

        return null;
    }
}
