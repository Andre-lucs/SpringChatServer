package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.db.PublicRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicRoomRepository extends JpaRepository<PublicRoom, String> {
}
