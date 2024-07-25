package org.andrelucs.SpringChatServer.controller;

import org.andrelucs.SpringChatServer.model.dto.ExceptionDTO;
import org.andrelucs.SpringChatServer.model.dto.MessageDTO;
import org.andrelucs.SpringChatServer.model.exception.NotFoundException;
import org.andrelucs.SpringChatServer.services.ChatRoomService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Date;

@Controller
public class ChatMessageController {

    private final ChatRoomService chatRoomService;

    public ChatMessageController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/send/{roomName}")
    @SendTo("/room/{roomName}")
    public MessageDTO sendMessage(@RequestBody MessageDTO message,
                                  @DestinationVariable String roomName,
                                  @Payload String msg,
                                  @Header("simpSessionId") String sessionId)
            throws NotFoundException {

        if (!chatRoomService.existsAndIsActive(roomName)){
            throw new NotFoundException("Passed Room does not exist: " + roomName);
        }
        chatRoomService.getLastRoomMessage().put(roomName, message);
        return message;
    }

    @MessageMapping("/send/user/{username}")
    @SendTo("/user/{username}")
    public MessageDTO sendMessageToUser(@RequestBody MessageDTO message,
                                        @DestinationVariable String username) throws Exception {
        System.out.println("sendMessageToUser");
        return message;
    }


    @MessageExceptionHandler
    public ExceptionDTO handleNotFoundException(NotFoundException e) {
        System.out.println("Exception: " + e.getMessage());

        return new ExceptionDTO(e.getMessage(), new Date(), e.getMessage());
    }
    @MessageExceptionHandler
    public ExceptionDTO handleException(Exception e) {
        System.out.println("Exception: " + e.getMessage());

        return new ExceptionDTO(e.getMessage(), new Date(), e.getMessage());
    }



}
