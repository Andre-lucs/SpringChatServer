package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.db.PrivateRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivateRoomRepository extends JpaRepository<PrivateRoom, Long> {

    Optional<PrivateRoom> findByUser1AndUser2(String user1, String user2);
    Optional<PrivateRoom> findByUser2AndUser1(String user2, String user1);
}
