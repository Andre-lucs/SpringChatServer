package org.andrelucs.SpringChatServer.model.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String s) {
        super(s);
    }
}
