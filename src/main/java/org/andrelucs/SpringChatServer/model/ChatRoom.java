package org.andrelucs.SpringChatServer.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.andrelucs.SpringChatServer.model.dto.ChatRoomDTO;

import java.io.Serializable;
import java.util.Objects;
@Entity
@Table(name = "CHATROOMS")
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME", unique = true)
    private String name;
    @Nonnull
    private Boolean active;
    public ChatRoom(){
        active = true;
    }
    public ChatRoom(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    public ChatRoom(ChatRoomDTO room) {
        setRoomName(room.getName());
        setActive(room.isActive());
        active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return name;
    }

    public void setRoomName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(name, chatRoom.name) && Objects.equals(active, chatRoom.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, active);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
