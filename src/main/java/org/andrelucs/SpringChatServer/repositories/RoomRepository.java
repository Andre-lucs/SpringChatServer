package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> getChatRoomByName(String name);
}
