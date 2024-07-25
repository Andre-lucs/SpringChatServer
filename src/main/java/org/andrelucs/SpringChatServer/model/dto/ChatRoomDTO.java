package org.andrelucs.SpringChatServer.model.dto;

import org.andrelucs.SpringChatServer.model.ChatRoom;

import java.util.Objects;

public class ChatRoomDTO {
    private String name;
    private boolean active;
    private Integer activeUsers;
    private MessageDTO lastMessage;
    public ChatRoomDTO(ChatRoom room) {
        this.name = room.getRoomName();
        this.active = room.isActive();
    }

    public ChatRoomDTO(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public ChatRoomDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomDTO that = (ChatRoomDTO) o;
        return active == that.active && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, active);
    }

    public Integer getActiveUsers() {
        if(activeUsers == null)return 0;
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
        if(activeUsers<0) this.activeUsers = 0;
    }

    public MessageDTO getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDTO lastMessage) {
        this.lastMessage = lastMessage;
    }
}
