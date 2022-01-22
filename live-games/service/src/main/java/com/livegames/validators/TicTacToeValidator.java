package com.livegames.validators;

import com.livegames.model.Room;
import com.livegames.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TicTacToeValidator extends Validator{

    @Resource(name = "RoomsMap")
    Map<String, Room> roomsMap;

    private final int MAX_PLAYERS_COUNT=2;

    @Override
    public String validate(User user, String roomId) {
        if (!roomsMap.containsKey(roomId)) {
            return "INVALID";
        }
        if (roomsMap.get(roomId).getUserMap().size() >= MAX_PLAYERS_COUNT) {
            return "FULL";
        }
        HashMap<String, User> userMap = roomsMap.get(roomId).getUserMap();
        if(userMap.containsKey(user.getUserName())){
            return "USERNAME ALREADY EXISTS";
        }
        return "VALID";
    }
}
