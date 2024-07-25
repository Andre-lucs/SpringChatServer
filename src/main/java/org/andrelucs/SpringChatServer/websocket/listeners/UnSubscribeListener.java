package org.andrelucs.SpringChatServer.websocket.listeners;

import org.andrelucs.SpringChatServer.services.RoomUserCount;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class UnSubscribeListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private RoomUserCount roomUserCount;

    public UnSubscribeListener(RoomUserCount roomUserCount) {
        this.roomUserCount = roomUserCount;
    }

    private Logger logger = Logger.getLogger(String.valueOf(UnSubscribeListener.class));
    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        System.out.println(event);
        var idMatcher = SubscribeListener.IdPattern.matcher(event.toString());
        if(idMatcher.find()){
            String userId = idMatcher.group(1);
            roomUserCount.removeUser(userId);
            logger.info("User unsub " + userId );
        }
    }



    @Override
    public boolean supportsAsyncExecution() {
        logger.log(Level.ALL, "asyncexec");
        System.out.println("asdada");
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
