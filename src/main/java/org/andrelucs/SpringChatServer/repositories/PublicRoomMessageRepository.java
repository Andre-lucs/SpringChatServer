package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.db.PublicRoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicRoomMessageRepository extends JpaRepository<PublicRoomMessage, Long> {
}
