package com.livegames.host_join.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.livegames.configuration.WebApplicationConfig;
import com.livegames.model.CreateRoomRQ;
import com.livegames.model.Room;
import com.livegames.model.User;
import com.livegames.model.User.MessageType;

import com.livegames.validators.ValidatorFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Data
@Controller
public class HostJoinController {

    @Resource(name = "RoomsMap")
    Map<String, Room> roomsMap;

    @PostMapping(value = "/createRoom")
    @ResponseBody
    public String hostGame(@RequestBody CreateRoomRQ createRoomRQ) {
        String roomId = generateRoomId();
        while(roomsMap.containsKey(roomId)){
            roomId = generateRoomId();
        }
        Room room = new Room();
        room.setGameType(createRoomRQ.getGameType());
        room.setRoomId(roomId);
        HashMap<String, User> userMap = new HashMap<>();
//        userMap.put(createRoomRQ.getUser().getUserName(), createRoomRQ.getUser());
        room.setUserMap(userMap);
        roomsMap.put(roomId, room);
        return "/"+room.getGameType()+"/"+roomId;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String sample() {
        return "index";
    }

    @PostMapping("/validate/{gameName}/{key}")
    @ResponseBody
    public String validate(@RequestBody User user, @PathVariable String key, @PathVariable String gameName) {
        return ValidatorFactory.getValidator(gameName).validate(user, key);
    }

    @MessageMapping("/joingame/{roomId}")
    @SendTo("/topic/public")
    public User joinGame(@Payload User user, @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor) {
        roomsMap.get(roomId).getUserMap().put(user.getUserName(), user);
        headerAccessor.getSessionAttributes().put("userName", user.getUserName());
        headerAccessor.getSessionAttributes().put("roomId", roomId);
        return user;
    }

    private String generateRoomId() {
        int roomIdLength = 4;
        StringBuilder result    = new StringBuilder();
        String characters       = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int charactersLength = characters.length();
        for ( var i = 0; i < roomIdLength; i++ ) {
            result.append(characters.charAt((int) Math.floor(Math.random() * charactersLength)));
        }
        return result.toString();
    }


}