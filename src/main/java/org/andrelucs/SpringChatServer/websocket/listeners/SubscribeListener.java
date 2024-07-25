package org.andrelucs.SpringChatServer.websocket.listeners;

import org.andrelucs.SpringChatServer.services.ChatRoomService;
import org.andrelucs.SpringChatServer.services.RoomUserCount;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


@Component
public class SubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    private final ChatRoomService chatRoomService;
    private RoomUserCount roomUserCount;

    public SubscribeListener(RoomUserCount roomUserCount, ChatRoomService chatRoomService) {
        this.roomUserCount = roomUserCount;
        this.chatRoomService = chatRoomService;
    }

    private Logger logger = Logger.getLogger(String.valueOf(SubscribeListener.class));
    public static final Pattern DestinationPattern = Pattern.compile("destination=\\[/room/([a-zA-Z0-9]*)]}", Pattern.CASE_INSENSITIVE);
    public static final Pattern IdPattern = Pattern.compile("simpSessionId=([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        logger.log(Level.ALL, event.toString());
        var matcher = DestinationPattern.matcher(event.toString());
        var idmatcher = IdPattern.matcher(event.toString());
        if(matcher.find() && idmatcher.find()){
            String roomName = matcher.group(1);

            //if(!chatRoomService.existsAndIsActive(roomName)) throw new NotFoundException("Chat room " + roomName + "not found");
            String id = idmatcher.group(1);

            logger.info("id: "+id+" subscribed to " + roomName);
            roomUserCount.addUser(roomName, id);
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        logger.log(Level.ALL, "asyncexec");
        System.out.println("asdada");
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
