package org.andrelucs.SpringChatServer.model.dto;

import java.util.Date;

public record MessageDTO(String sender, String content, Date date) {
}
