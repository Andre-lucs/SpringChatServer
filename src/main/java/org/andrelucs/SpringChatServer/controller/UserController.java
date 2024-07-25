package org.andrelucs.SpringChatServer.controller;

import lombok.RequiredArgsConstructor;
import org.andrelucs.SpringChatServer.model.db.PrivateRoomMessage;
import org.andrelucs.SpringChatServer.model.dto.message.PrivateMessageDTO;
import org.andrelucs.SpringChatServer.model.dto.StatusDTO;
import org.andrelucs.SpringChatServer.model.dto.message.NotificationMessageDTO;
import org.andrelucs.SpringChatServer.model.exception.CreateRoomException;
import org.andrelucs.SpringChatServer.model.exception.UserNotFoundException;
import org.andrelucs.SpringChatServer.services.PrivateRoomService;
import org.andrelucs.SpringChatServer.services.PublicRoomService;
import org.andrelucs.SpringChatServer.services.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class UserController {

    Logger logger = Logger.getLogger(String.valueOf(UserController.class));

    private final SimpMessagingTemplate template;
    private final UserService service;
    private final PublicRoomService roomService;
    private final PrivateRoomService privateRoomService;

    @MessageMapping("/user/send")
    public void sendMessage(@Payload PrivateMessageDTO message){
        try {
            PrivateRoomMessage privateMsg = privateRoomService.sendPrivateMessage(message);
            System.out.println("sendMessage "+message);

            template.convertAndSendToUser(message.getTargetId(),
                    "/queue/messages",
                    new PrivateMessageDTO(privateMsg.getSenderId(), privateMsg.getRecieverId(), privateMsg.getMessageBody(), privateMsg.getMoment()));
        }catch (CreateRoomException | UserNotFoundException e){
            logger.warning(e.getMessage());
        }
    }


    @MessageMapping("/user/status")
    public void changeStatus(@Payload StatusDTO status){
        if(service.updateStatus(status)){
            roomService.sendConnectNotificationToRooms(status);
            roomService.removeUserFromRoom(status.username());
            service.sendToRelatedUsers(status.username(), new NotificationMessageDTO(String.format("User %s is %s", status.username(), status.currentStatus())));
        }
    }


}
