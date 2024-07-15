package org.andrelucs.SpringChatServer.model.dto;

import java.util.Date;

public record ExceptionDTO(String message, Date moment, String description) {
}
