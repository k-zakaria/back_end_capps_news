package org.capps.news.web.exception.user;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String s) {
        super(s);
    }
}
