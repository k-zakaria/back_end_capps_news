package org.capps.news.web.exception.comment;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String commentNotFound) {
        super(commentNotFound);
    }
}
