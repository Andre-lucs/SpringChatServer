package org.andrelucs.SpringChatServer.model.dto.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.andrelucs.SpringChatServer.model.db.PublicRoomMessage;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicRoomMessageDTO extends MessageDTO {
    private String roomId;
    private String senderId;
    public PublicRoomMessageDTO(String content, String roomId, String moment) {
        super(content, MessageDtoType.ROOM_MESSAGE, moment);
        this.roomId = roomId;
    }

    public PublicRoomMessageDTO(){
        super(MessageDtoType.ROOM_MESSAGE);
    }

    public PublicRoomMessageDTO(PublicRoomMessage msg) {
        super(msg.getMessageBody(), MessageDtoType.ROOM_MESSAGE, msg.getMoment());
        this.senderId = msg.getSenderId();
        this.roomId = msg.getRoomId();
    }
}
