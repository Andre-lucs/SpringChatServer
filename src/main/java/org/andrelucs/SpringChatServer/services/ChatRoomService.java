package org.andrelucs.SpringChatServer.services;

import org.andrelucs.SpringChatServer.model.ChatRoom;
import org.andrelucs.SpringChatServer.model.dto.ChatRoomDTO;
import org.andrelucs.SpringChatServer.model.dto.MessageDTO;
import org.andrelucs.SpringChatServer.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private RoomRepository repository;

    private RoomUserCount roomsUsers;

    private final Map<String, MessageDTO> lastRoomMessage = new HashMap<>();

    public ChatRoomService(RoomRepository repository, RoomUserCount roomsUsers) {
        this.repository = repository;
        this.roomsUsers = roomsUsers;
    }

    public List<ChatRoomDTO> getAllActiveRooms() {
        var chatRoomsDtos = repository.findAll().stream().filter(ChatRoom::isActive).map(ChatRoomDTO::new).collect(Collectors.toList());
        chatRoomsDtos.forEach(i-> {
            i.setActiveUsers(roomsUsers.getUserCount(i.getName()));
            if(lastRoomMessage.containsKey(i.getName())) i.setLastMessage(lastRoomMessage.get(i.getName()));
        });
        return chatRoomsDtos;
    }

    public List<ChatRoomDTO> getAllRooms(){
        return repository.findAll().stream().map(ChatRoomDTO::new).collect(Collectors.toList());
    }
    public ChatRoomDTO getRoomById(Long id){
        ChatRoom room = repository.findById(id).orElse(null);
        return room != null ? new ChatRoomDTO(room) : null;
    }
    public ChatRoomDTO createRoom(ChatRoomDTO room){
        var chatRoom = new ChatRoom(room);
        repository.save(chatRoom);
        return new ChatRoomDTO(chatRoom);
    }
    public ChatRoomDTO updateRoom(Long id, ChatRoomDTO room){
        if(!repository.existsById(id)) return null;
        return new ChatRoomDTO(repository.save(new ChatRoom(room)));
    }
    public void deleteRoom(Long id){
        repository.deleteById(id);
    }

    public Boolean existsAndIsActive(String name){
        var chatRoom = repository.getChatRoomByName(name);
        return chatRoom.isPresent() && chatRoom.get().isActive();
    }

    public Map<String, MessageDTO> getLastRoomMessage() {
        return lastRoomMessage;
    }
}
