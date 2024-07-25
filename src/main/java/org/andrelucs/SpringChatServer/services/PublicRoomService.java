package org.andrelucs.SpringChatServer.services;

import lombok.RequiredArgsConstructor;
import org.andrelucs.SpringChatServer.model.db.PublicRoomMessage;
import org.andrelucs.SpringChatServer.model.db.PublicRoomUser;
import org.andrelucs.SpringChatServer.model.dto.StatusDTO;
import org.andrelucs.SpringChatServer.model.dto.message.NotificationMessageDTO;
import org.andrelucs.SpringChatServer.model.dto.message.PublicRoomMessageDTO;
import org.andrelucs.SpringChatServer.model.exception.NotFoundException;
import org.andrelucs.SpringChatServer.repositories.PublicRoomMessageRepository;
import org.andrelucs.SpringChatServer.repositories.PublicRoomRepository;
import org.andrelucs.SpringChatServer.repositories.PublicRoomUsersRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicRoomService {

    private final SimpMessagingTemplate template;
    private final PublicRoomRepository roomRepository;
    private final PublicRoomMessageRepository messageRepository;
    private final PublicRoomUsersRepository roomUsersRepository;

    public void sendConnectNotificationToRooms(StatusDTO status) {
        var message = String.format("User %s has %s the room",
                status.username(),
                (status.currentStatus().value()) ? "connected to" : "disconnected from");
        var notification = new NotificationMessageDTO(message);

        List<String> rooms = getUserRooms(status.username());

        for (String room : rooms) {
            template.convertAndSend("/room/"+room+"/queue/notifications", notification);
        }
    }

    public List<String> getUserRooms(String username) {
        var rooms = roomUsersRepository.getRoomsByUserId(username);
        //implement repository query
        return List.of("general_chat");
    }

    public List<String> getRoomUsers(String roomId){
        return roomUsersRepository.getPublicRoomUsersByRoomId(roomId).stream().map(PublicRoomUser::getUserId).toList();
    }

    public PublicRoomMessage sendMessage(PublicRoomMessageDTO message) throws NotFoundException {
        if (roomRepository.existsById(message.getRoomId())){
            return messageRepository.save(new PublicRoomMessage(message));
        }
        throw new NotFoundException("Room does not exist");
    }

    public boolean addUserToRoom(String userId, String roomId){
        if (roomUsersRepository.existsByUserIdAndRoomId(userId, roomId))return false;
        var saved = roomUsersRepository.save(PublicRoomUser.builder().userId(userId).roomId(roomId).last_seen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).build());
        return saved.getRoomId().equals(roomId) && saved.getId() != null;
    }

    @Transactional
    public boolean removeUserFromRoom(String userId){
        var deletedLines = roomUsersRepository.deleteAllByUserId(userId);
        return deletedLines > 0;
    }

    @Transactional
    public boolean removeUserFromRoom(String userId, String roomId){
        if (!roomUsersRepository.existsByUserIdAndRoomId(userId, roomId)) return false;
        var deletedLines = roomUsersRepository.deleteByUserIdAndRoomId(userId, roomId);
        return deletedLines > 0;
    }
}
