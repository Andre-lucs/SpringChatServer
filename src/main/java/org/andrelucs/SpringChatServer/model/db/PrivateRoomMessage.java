package org.andrelucs.SpringChatServer.model.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "private_rooms_messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PrivateRoomMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private String senderId;
    private String recieverId;
    private String messageBody;
    private String moment;
}
