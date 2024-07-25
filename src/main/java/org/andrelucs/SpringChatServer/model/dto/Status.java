package org.andrelucs.SpringChatServer.model.dto;

public enum Status{
    OFFLINE(false),
    ONLINE(true);

    private final boolean online;

    Status(boolean online){
        this.online = online;
    }

    public Boolean value() {
        return this.online;
    }
}
