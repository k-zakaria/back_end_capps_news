package org.capps.news.web.exception;

public class FieldCannotBeNullException extends RuntimeException{
    public FieldCannotBeNullException(String message) {
        super(message);
    }
}
