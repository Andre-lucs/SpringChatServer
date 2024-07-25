package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.db.PrivateRoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateRoomMessageRepository extends JpaRepository<PrivateRoomMessage, Long> {
}
