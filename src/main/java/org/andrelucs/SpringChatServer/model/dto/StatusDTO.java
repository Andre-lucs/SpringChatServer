package org.andrelucs.SpringChatServer.model.dto;

import lombok.Builder;

//for security will also have a token
@Builder
public record StatusDTO(Status currentStatus, String username) {
}

