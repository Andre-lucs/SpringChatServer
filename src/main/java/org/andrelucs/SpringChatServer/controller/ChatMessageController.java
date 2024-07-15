package org.andrelucs.SpringChatServer.controller;

import org.andrelucs.SpringChatServer.model.dto.MessageDTO;
import org.andrelucs.SpringChatServer.model.exception.NotFoundException;
import org.andrelucs.SpringChatServer.services.ChatRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatMessageController {

    private ChatRoomService chatRoomService;

    public ChatMessageController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/send/{roomName}")
    @SendTo("/room/{roomName}")
    public MessageDTO sendMessage(@RequestBody MessageDTO message, @DestinationVariable String roomName) throws NotFoundException {
        System.out.println("Received message: " + message);
        System.out.println("To Room: " + roomName);
        if (!chatRoomService.existsAndIsActive(roomName)){
            throw new NotFoundException("Passed Room does not exist: " + roomName);
        }
        //store
        return message;
    }

}
