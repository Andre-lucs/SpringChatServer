package org.andrelucs.SpringChatServer.model;

import java.util.Date;

public record Message(String sender, String content, Date date) {
}
