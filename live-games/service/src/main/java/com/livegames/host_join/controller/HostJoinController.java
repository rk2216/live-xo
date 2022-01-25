package com.livegames.host_join.controller;

import java.util.HashMap;
import java.util.Map;

import com.livegames.model.CreateRoomRQ;
import com.livegames.model.JoinGameRS;
import com.livegames.model.Room;
import com.livegames.model.User;
import com.livegames.validators.ValidatorFactory;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public HostJoinController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

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
        room.setUserMap(userMap);
        room.setHost(createRoomRQ.getUser());
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
    public void joinGame(@Payload User user, @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor) {
        JoinGameRS joinGameRS = new JoinGameRS();
        joinGameRS.setHost(roomsMap.get(roomId).getHost());
        joinGameRS.setUser(user);
        roomsMap.get(roomId).getUserMap().put(user.getUserName(), user);
        joinGameRS.setMembers(roomsMap.get(roomId).getUserMap().keySet().toArray(new String[0]));
        headerAccessor.getSessionAttributes().put("userName", user.getUserName());
        headerAccessor.getSessionAttributes().put("roomId", roomId);
        messagingTemplate.convertAndSend("/topic/public/" + roomId, joinGameRS);
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