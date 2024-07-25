package org.andrelucs.SpringChatServer.model.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "CHAT_USERS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    private String username;
    private String password;
    private Boolean online;

    public boolean isOnline() {return this.online;}

}
