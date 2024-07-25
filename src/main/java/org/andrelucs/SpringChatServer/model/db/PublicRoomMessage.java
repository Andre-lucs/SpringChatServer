package org.andrelucs.SpringChatServer.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.andrelucs.SpringChatServer.model.dto.message.PublicRoomMessageDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "public_room_messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PublicRoomMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;
    private String senderId;
    private String messageBody;
    private String moment;

    public PublicRoomMessage(PublicRoomMessageDTO msg){
        this.roomId = msg.getRoomId();
        this.senderId = msg.getSenderId();
        this.messageBody = msg.getContent();
        if (moment == null || moment.isEmpty())
            this.moment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        else
            this.moment = msg.getMoment();
    }
}
