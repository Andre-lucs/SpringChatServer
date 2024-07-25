package org.andrelucs.SpringChatServer.model.dto.message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationMessageDTO extends MessageDTO {
    public NotificationMessageDTO(String content) {
        super(content, MessageDtoType.NOTIFICATION, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
