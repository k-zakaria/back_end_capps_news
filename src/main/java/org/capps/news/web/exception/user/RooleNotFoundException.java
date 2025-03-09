package org.capps.news.web.exception.user;

public class RooleNotFoundException extends RuntimeException {
    public RooleNotFoundException(String roleNotFound) {
        super(roleNotFound);
    }
}
