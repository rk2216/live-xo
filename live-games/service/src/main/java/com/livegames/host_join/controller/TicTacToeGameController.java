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
    private Character X = 'X', O = 'O', TIE = 'T';

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
        ticTacToeGameRS.setCurrentTurn(ticTacToeGameRS.getCurrentTurn().equals(X) ? O : X);
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
        ticTacToeGameRS.setCurrentTurn(X);
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
            players.put(user.getUserName(), players.entrySet().iterator().next().getValue().equals(X) ? O : X);
            ticTacToeGameRS.setPlayers(players);
            gameMap.put(roomId, ticTacToeGameRS);
        } else {
            ticTacToeGameRS = new TicTacToeGameRS();
            Map<String, Character> players = new HashMap<>();
            players.put(user.getUserName(), X);
            ticTacToeGameRS.setBoard(new ArrayList<>(Collections.nCopies(9, null)));
            ticTacToeGameRS.setPlayers(players);
            ticTacToeGameRS.setCurrentTurn(X);
            ticTacToeGameRS.setWinner(null);
            ticTacToeGameRS.setUserMap(roomsMap.get(roomId).getUserMap());
            gameMap.put(roomId, ticTacToeGameRS);
        }
        messagingTemplate.convertAndSend("/topic/public/" + roomId, ticTacToeGameRS);
    }

    Character findWinner(List<Character> board) {
        //Column Winner
        for (int col = 0; col <= 2; col++) {
            if (board.get(col) != null && board.get(col + 3) != null && board.get(col + 6) != null) {
                if (board.get(col) == board.get(col + 3) && board.get(col + 3) == board.get(col + 6)) {
                    return board.get(col);
                }
            }
        }
        //Row Winner
        for (int row = 0; row <= 6; row += 3) {
            if (board.get(row) != null && board.get(row + 1) != null && board.get(row + 2) != null) {
                if (board.get(row) == board.get(row + 1) && board.get(row + 1) == board.get(row + 2)) {
                    return board.get(row);
                }
            }
        }
        //Diagonal Winner
        if (board.get(0) != null && board.get(4) != null && board.get(8) != null) {
            if (board.get(0) == board.get(4) && board.get(4) == board.get(8)) {
                return board.get(4);
            }
        }
        if (board.get(2) != null && board.get(4) != null && board.get(6) != null) {
            if (board.get(2) == board.get(4) && board.get(4) == board.get(6)) {
                return board.get(4);
            }
        }
        //Tie Condition
        boolean isBoardFull = true;
        for (int i = 0; i < 9; i++) {
            if (board.get(i) == null) {
                isBoardFull = false;
                break;
            }
        }
        if (isBoardFull) {
            return TIE;
        }
        return null;
    }
}
