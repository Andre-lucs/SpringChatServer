package org.andrelucs.SpringChatServer.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "public_rooms_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;
    private String userId;
    private String last_seen;
}
