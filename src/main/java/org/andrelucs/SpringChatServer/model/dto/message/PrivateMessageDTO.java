package org.andrelucs.SpringChatServer.model.dto.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrivateMessageDTO extends MessageDTO {
    private String targetId;
    private String senderId;


    public PrivateMessageDTO(String senderId, String targetId, String content, String moment) {
        super(content, MessageDtoType.USER_MESSAGE, moment);
        this.targetId = targetId;
        this.senderId = senderId;
    }
    public PrivateMessageDTO(){
        super(MessageDtoType.USER_MESSAGE);
    }
}
