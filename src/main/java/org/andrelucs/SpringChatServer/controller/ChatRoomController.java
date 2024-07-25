package org.andrelucs.SpringChatServer.controller;

import org.andrelucs.SpringChatServer.model.dto.ChatRoomDTO;
import org.andrelucs.SpringChatServer.services.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    private ChatRoomService service;

    public ChatRoomController(ChatRoomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDTO>> findAll(){
        return ResponseEntity.ok(service.getAllRooms());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ChatRoomDTO>> findALlActiveRooms(){
        return ResponseEntity.ok(service.getAllActiveRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.getRoomById(id));
    }

    @PostMapping
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody ChatRoomDTO room){
        return ResponseEntity.ok(service.createRoom(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatRoomDTO> updateRoom(@PathVariable Long id, @RequestBody ChatRoomDTO room){
        return ResponseEntity.ok(service.updateRoom(id, room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id){
        service.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

}
