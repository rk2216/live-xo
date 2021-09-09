package com.livegames.host_join.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.livegames.model.User;
import com.livegames.model.User.MessageType;

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
    HashMap<String, List<User>> map = new HashMap<String, List<User>>();

    @MessageMapping("/hostgame")
    public User hostGame(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        String key = "123";
        List<User> users = new ArrayList<>();
        users.add(user);
        map.put(key, users);
        headerAccessor.getSessionAttributes().put("username", user.getSender());
        return user;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String sample() {
        return "index";
    }

    @PostMapping("/validate/{key}")
    @ResponseBody
    public MessageType validate(@RequestBody User user, @PathVariable String key) {
        if (map.getOrDefault(key, new ArrayList<User>()).size() == 0) {
            return User.MessageType.INVALID;
        }
        if (map.get(key).size() >= 2) {
            return User.MessageType.FULL;
        }
        return User.MessageType.VALID;
    }

    @MessageMapping("/joingame/{key}")
    @SendTo("/topic/public")
    public User joinGame(@Payload User user, @DestinationVariable String key,
            SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        if (map.getOrDefault(key, new ArrayList<User>()).size() == 0) {
            user.setContent("Invalid Link");
            user.setType(User.MessageType.INVALID);
            return user;
        }
        if (map.get(key).size() >= 2) {
            user.setType(User.MessageType.FULL);
            return user;
        }
        List<User> users = map.get(key);
        users.add(user);
        map.put(key, users);
        headerAccessor.getSessionAttributes().put("username", user.getSender());
        return user;
    }
}