package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.db.PublicRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublicRoomUsersRepository extends JpaRepository<PublicRoomUser, Long> {

    List<PublicRoomUser> getPublicRoomUsersByRoomId(String roomId);

    @Query("select r.roomId from PublicRoomUser r where r.userId = ?1")
    List<String> getRoomsByUserId(String userId);

    Boolean existsByUserIdAndRoomId(String userId, String roomId);

    @Modifying
    Integer deleteByUserIdAndRoomId(String userId, String roomId);

    @Modifying
    Integer deleteAllByUserId(String userId);
}
