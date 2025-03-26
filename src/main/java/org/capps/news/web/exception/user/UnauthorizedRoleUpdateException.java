package org.capps.news.web.exception.user;

public class UnauthorizedRoleUpdateException extends RuntimeException {
    public UnauthorizedRoleUpdateException(String s) {
        super(s);
    }
}
