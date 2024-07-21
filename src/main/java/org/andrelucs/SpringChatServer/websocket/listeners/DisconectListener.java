package org.andrelucs.SpringChatServer.websocket.listeners;

import org.andrelucs.SpringChatServer.services.RoomUserCount;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Component
public class DisconectListener implements ApplicationListener<SessionDisconnectEvent> {

    private RoomUserCount roomUserCount;

    public DisconectListener(RoomUserCount roomUserCount) {
        this.roomUserCount = roomUserCount;
    }
    private final Pattern disconectIdParttern = Pattern.compile("sessionId=([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
    private final Logger logger = Logger.getLogger(String.valueOf(DisconectListener.class));
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        logger.info(event.toString());
        System.out.println("disconect " + event);
        var idMatcher = disconectIdParttern.matcher(event.toString());
        if(idMatcher.find()){
            String userId = idMatcher.group(1);
            System.out.println("User disconect " + userId );
            roomUserCount.removeUser(userId);
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        logger.log(Level.ALL, "asyncexec");
        System.out.println("asdada");
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
