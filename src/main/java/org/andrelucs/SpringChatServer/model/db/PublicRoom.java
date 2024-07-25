package org.andrelucs.SpringChatServer.model.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "public_rooms")
@NoArgsConstructor
public class PublicRoom {
    @Id
    private String room_name;
}
