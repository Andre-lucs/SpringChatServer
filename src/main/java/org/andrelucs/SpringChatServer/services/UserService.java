package org.andrelucs.SpringChatServer.services;

import lombok.RequiredArgsConstructor;
import org.andrelucs.SpringChatServer.model.db.User;
import org.andrelucs.SpringChatServer.model.dto.message.MessageDTO;
import org.andrelucs.SpringChatServer.model.dto.StatusDTO;
import org.andrelucs.SpringChatServer.model.dto.message.NotificationMessageDTO;
import org.andrelucs.SpringChatServer.repositories.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;

    public User findUser(String username){
        return userRepository.findById(username).orElse(null);
    }

    public boolean existsUser(String... users){
        for(String username : users){
            if(!userRepository.existsById(username)){
                return false;
            }
        }
        return true;
    }

    @Transactional
    public boolean updateStatus(StatusDTO status) {
        var updated = userRepository.updateStatus(status.username(), status.currentStatus().value());
        System.out.println(updated+status.username()+status.currentStatus().toString());
        return updated >0;
    }

    public void sendToRelatedUsers(String username, MessageDTO message) {
        List<String> relatedUsers = getRelatedUsers(username, true);
        System.out.println(relatedUsers.size());
        relatedUsers.remove(username); // remove the sender from the list
        relatedUsers.forEach(user -> {
            System.out.println("sent to "+user);
            if (message instanceof NotificationMessageDTO)
                template.convertAndSendToUser(user, "/queue/notifications", message);
            else
                template.convertAndSendToUser(user, "/queue/messages", message);
        });
    }

    public List<String> getRelatedUsers(String username, boolean onlineOnly){
        return userRepository.getRelatedUsers(username, onlineOnly);
    }
}
