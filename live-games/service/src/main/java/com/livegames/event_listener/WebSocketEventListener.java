package com.livegames.event_listener;

import com.livegames.model.JoinGameRS;
import com.livegames.model.Room;
import com.livegames.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource(name = "RoomsMap")
    Map<String, Room> roomsMap;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        logger.info("User Disconnected : " + userName);
        if (userName != null) {
            logger.info("User Disconnected : " + userName);

            Room currentRoom = roomsMap.get(roomId);
            User user = new User();
            user.setType(User.MessageType.LEAVE);
            user.setUserName(userName);
            currentRoom.getUserMap().remove(userName);
            if(currentRoom.getUserMap().isEmpty()){
                roomsMap.remove(roomId);
                return;
            }
            User previousHost = currentRoom.getHost();
            if(previousHost.getUserName().equals(userName)) {
                currentRoom.setHost(currentRoom.getUserMap().entrySet().iterator().next().getValue());
            }

            JoinGameRS joinGameRS = new JoinGameRS();
            joinGameRS.setUser(user);
            joinGameRS.setHost(currentRoom.getHost());
            joinGameRS.setMembers(roomsMap.get(roomId).getUserMap().keySet().toArray(new String[0]));
            messagingTemplate.convertAndSend("/topic/public/" + roomId, joinGameRS);
        }
    }
}
