package com.livegames.host_join.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.livegames.model.CreateRoomRQ;
import com.livegames.model.Room;
import com.livegames.model.User;
import com.livegames.model.User.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class HostJoinController {
    HashMap<String, List<User>> map = new HashMap<>();
    HashMap<String, Room> roomsMap;

    @Autowired
    public HostJoinController(@Qualifier("RoomsMap") HashMap<String, Room> roomsMap){
        this.roomsMap=roomsMap;
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
        List<User> users = new ArrayList<>();
        users.add(createRoomRQ.getUser());
        room.setUserList(users);
        roomsMap.put(roomId, room);
        return "/"+room.getGameType()+"/"+roomId;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String sample() {
        return "index";
    }

//    @PostMapping("/validate/{key}")
//    @ResponseBody
//    public MessageType validate(@RequestBody User user, @PathVariable String key) {
//        if (map.getOrDefault(key, new ArrayList<User>()).size() == 0) {
//            return User.MessageType.INVALID;
//        }
//        if (map.get(key).size() >= 2) {
//            return User.MessageType.FULL;
//        }
//        return User.MessageType.VALID;
//    }

    @PostMapping("/validate/{gameName}/{key}")
    @ResponseBody
    public String validate(@RequestBody User user, @PathVariable String key, @PathVariable String gameName) {
        if (!roomsMap.containsKey(key)) {
            return "INVALID";
        }
        if (roomsMap.get(key).getUserList().size() >= 2) {
            return "FULL";
        }
        List<User> roomUsers = roomsMap.get(key).getUserList();
        for(int i=0;i<roomUsers.size();i++){
            if(roomUsers.get(i).getUserName().equals(user.getUserName())){
                return "USERNAME ALREADY EXISTS";
            }
        }
        return "VALID";
    }

    @MessageMapping("/joingame/{key}")
    @SendTo("/topic/public")
    public User joinGame(@Payload User user, @DestinationVariable String key,
            SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        List<User> users = map.get(key);
        users.add(user);
        map.put(key, users);
        headerAccessor.getSessionAttributes().put("username", user.getUserName());
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