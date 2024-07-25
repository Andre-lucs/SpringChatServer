package org.andrelucs.SpringChatServer.services;

import lombok.RequiredArgsConstructor;
import org.andrelucs.SpringChatServer.model.db.PrivateRoom;
import org.andrelucs.SpringChatServer.model.db.PrivateRoomMessage;
import org.andrelucs.SpringChatServer.model.dto.message.PrivateMessageDTO;
import org.andrelucs.SpringChatServer.model.exception.CreateRoomException;
import org.andrelucs.SpringChatServer.model.exception.UserNotFoundException;
import org.andrelucs.SpringChatServer.repositories.PrivateRoomMessageRepository;
import org.andrelucs.SpringChatServer.repositories.PrivateRoomRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateRoomService {
    private final PrivateRoomRepository repository;
    private final PrivateRoomMessageRepository privateRoomMessageRepository;
    private final UserService userService;


    public Optional<PrivateRoom> findByUsers(String u1, String u2, boolean create) {
        var foundRoom = Optional.ofNullable(repository.findByUser1AndUser2(u1, u2).orElse(repository.findByUser2AndUser1(u2,u1).orElse(null)));
        if(create && foundRoom.isEmpty()){
            System.out.println("creating a room");
            return Optional.of(repository.save(PrivateRoom.builder().user1(u1).user2(u2).build()));
        }
        System.out.println("found a room");
        return foundRoom;
    }

    public PrivateRoomMessage sendPrivateMessage(PrivateMessageDTO message) throws UserNotFoundException, CreateRoomException {
        if(userService.existsUser(message.getTargetId(), message.getSenderId())){
            PrivateRoom room = findByUsers(message.getSenderId(), message.getTargetId(), true).orElse(null);
            if(room == null){
                throw new CreateRoomException("Occurred an error at searching or creating a room");
            }

            PrivateRoomMessage newMessage = PrivateRoomMessage.builder()
                    .roomId(room.getId())
                    .senderId(message.getSenderId())
                    .recieverId(message.getTargetId())
                    .messageBody(message.getContent())
                    .moment(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .build();
            return privateRoomMessageRepository.save(newMessage);
        }
        throw new UserNotFoundException("Passed users were not found.");
    }
}
