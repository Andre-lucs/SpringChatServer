package org.andrelucs.SpringChatServer.model.dto.message;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
public class MessageDTO{
    protected String content;
    @Setter(AccessLevel.NONE)
    protected final MessageDtoType type;
    private String moment;

    protected MessageDTO(MessageDtoType type){
        this.type = type;
    }

    public MessageDTO(){
        this.moment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) ;
        this.type = MessageDtoType.COMMAND_MESSAGE;
    }
}
