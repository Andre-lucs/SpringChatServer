package org.andrelucs.SpringChatServer.controller;

import lombok.RequiredArgsConstructor;
import org.andrelucs.SpringChatServer.model.db.PublicRoomMessage;
import org.andrelucs.SpringChatServer.model.dto.RoomEventDTO;
import org.andrelucs.SpringChatServer.model.dto.message.NotificationMessageDTO;
import org.andrelucs.SpringChatServer.model.dto.message.PublicRoomMessageDTO;
import org.andrelucs.SpringChatServer.services.PublicRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final SimpMessagingTemplate template;
    private final PublicRoomService service;

    // WebSocket

    @MessageMapping("/room/send")
    public void sendMessage(@Payload PublicRoomMessageDTO message) {
        try{
            PublicRoomMessage messageSent = service.sendMessage(message);
            PublicRoomMessageDTO messageDTO = new PublicRoomMessageDTO(messageSent);

            template.convertAndSend("/room/"+message.getRoomId()+"/queue/messages", messageDTO);
        }catch (Exception e){
            template.convertAndSendToUser(message.getSenderId(), "/queue/notifications", new NotificationMessageDTO("Failed to send message to room"));
            e.printStackTrace();
        }
    }

    @MessageMapping("/room/join")
    public void joinRoom(@Payload RoomEventDTO roomEvent) {
        System.out.println(roomEvent.username() + " entered " + roomEvent.roomId());
        var added = service.addUserToRoom(roomEvent.username(), roomEvent.roomId());
        if (added){
            template.convertAndSend("/room/"+roomEvent.roomId()+"/queue/notifications", new NotificationMessageDTO(roomEvent.username()+" has joined the chat room"));
            template.convertAndSendToUser(roomEvent.username(), "/queue/notifications", new NotificationMessageDTO("You have joined the room"));
        }else{
            System.out.println(roomEvent.username()+" hasnot joined");
        }
    }

    @MessageMapping("/room/leave")
    public void exitRoom(@Payload RoomEventDTO roomEvent) {
        System.out.println(roomEvent.username() + " left " + roomEvent.roomId());
        var removed = service.removeUserFromRoom(roomEvent.username(), roomEvent.roomId());
        if(removed){
            template.convertAndSend("/room/"+roomEvent.roomId()+"/queue/notifications", service.getRoomUsers(roomEvent.roomId()));
            template.convertAndSendToUser(roomEvent.username(), "/queue/notifications", new NotificationMessageDTO("You have left the room"));
        }

    }



    // REST

    @GetMapping("/api/rooms")
    public ResponseEntity<List<String>> getRooms() {
        return null;
        // Return a list of all available rooms
    }

    @GetMapping("/api/rooms/users")
    public ResponseEntity<List<String>> getUsersInRoom(String roomId) {
        return null;


        // Return a list of users currently in the specified room
    }
}
