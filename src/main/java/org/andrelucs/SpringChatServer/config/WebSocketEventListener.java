package org.andrelucs.SpringChatServer.config;

import lombok.RequiredArgsConstructor;
import org.andrelucs.SpringChatServer.controller.RoomController;
import org.andrelucs.SpringChatServer.controller.UserController;
import org.andrelucs.SpringChatServer.model.dto.RoomEventDTO;
import org.andrelucs.SpringChatServer.model.dto.Status;
import org.andrelucs.SpringChatServer.model.dto.StatusDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserController userController;
    private final RoomController roomController;

    @EventListener
    public void handleWebSocketDisconnectEvent(SessionDisconnectEvent event) {
        userOffline(event.getMessage());
    }

    @EventListener
    public void handleWebSocketSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        var str = event.toString();
        str = str.substring(str.indexOf("destination=[") + 13);
        var destination = str.substring(0, str.indexOf("]"));
        var roomId = regexQueueMessages("room", destination);
        if(!roomId.isEmpty()){
            String username = (String) accessor.getSessionAttributes().get("username");
            if(username == null || username.isEmpty()) return;
            System.out.println(username +"will room join");
            System.out.println("usr");
            roomController.joinRoom(new RoomEventDTO(username, roomId));
        }
        var username = regexQueueMessages("user", destination);
        if (!username.isEmpty()) {
            System.out.println(username+" subscribed");
            accessor.getSessionAttributes().put("username", username);
            updateUserStatus(username, Status.ONLINE);
        }

    }

    private String regexQueueMessages(String s, String dest){
        var m = Pattern.compile(String.format("/%s/([a-zA-Z0-9_]+)/queue/messages", s)).matcher(dest);
        if(m.find()){
            return m.group(1);
        }
        return "";
    }

    private void userOffline(Message<byte[]> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String username = (String) accessor.getSessionAttributes().get("username");
        if(username != null && !username.isEmpty()){
            updateUserStatus(username, Status.OFFLINE);
            System.out.println("Disconnect: " + username);
            accessor.getSessionAttributes().remove("username");
        }
    }
    private void updateUserStatus(String username , Status status){
        userController.changeStatus(StatusDTO.builder().username(username).currentStatus(status).build());
    }
}
