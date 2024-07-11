package org.andrelucs.SpringChatServer.controller;

import org.andrelucs.SpringChatServer.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatMessageController {

    @MessageMapping("/send/{roomId}")
    @SendTo("/room/{roomId}")
    public Message sendMessage(@RequestBody Message message, @PathVariable String roomId) {
        System.out.println("Received message: " + message);
        //verification
        //store
        return message;
    }

}
