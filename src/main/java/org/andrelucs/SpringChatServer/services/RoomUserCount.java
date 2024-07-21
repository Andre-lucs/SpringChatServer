package org.andrelucs.SpringChatServer.services;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoomUserCount extends HashMap<String, List<String>> {// RoomName | UserIds

    public RoomUserCount(Map<? extends String, ? extends List<String>> m) {
        super(m);
    }

    public int getUserCount(String key){
        return getOrDefault(key, List.of()).size();  // Use orDefault to handle null values gracefully. 0 if key not found.
    }

    public void removeUser(String userId) {
        String modifiedRoom = "";
        for (var entry: entrySet()) {
            if(!entry.getValue().contains(userId))continue;
            var copy = new ArrayList<>(entry.getValue());
            copy.remove(userId);
            modifiedRoom = entry.getKey();
            computeIfPresent(entry.getKey(), (s, strings) -> copy);
        }
        if(!modifiedRoom.isBlank())
            sendRefreshMessage(modifiedRoom);
    }

    public void addUser(String roomName, String userId){
        computeIfPresent(roomName, ((s, list) -> {
            List<String> copy = new ArrayList<>(list);
            copy.add(userId);
            return copy;
        }));
        putIfAbsent(roomName, List.of(userId));
        sendRefreshMessage(roomName);
    }

    private void sendRefreshMessage(String roomName){

    }
}
