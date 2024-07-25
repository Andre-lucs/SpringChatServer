package org.andrelucs.SpringChatServer.repositories;

import org.andrelucs.SpringChatServer.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Modifying
    @Query("UPDATE User u set u.online = ?2 where u.username = ?1")
    int updateStatus(String username, Boolean status);

    @Query("select pr.user2 from User u " +
            "join PrivateRoom pr ON u.username = pr.user1 " +
            "where u.username = ?1 and ((u.online = true and ?2 = true) or (?2 = false)) " +
            "union " +
            "select pr.user1 from User u " +
            "join PrivateRoom pr on u.username = pr.user2 " +
            "where u.username = ?1 and ((u.online = true and ?2 = true) or (?2 = false))")
    List<String> getRelatedUsers(String username, boolean onlineOnly);
}
