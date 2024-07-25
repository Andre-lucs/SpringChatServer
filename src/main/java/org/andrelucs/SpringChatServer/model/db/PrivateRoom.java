package org.andrelucs.SpringChatServer.model.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "private_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PrivateRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user1;
    private String user2;
}
