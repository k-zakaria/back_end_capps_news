package org.capps.news.web.exception.tag;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException(String tagNotFound) {
        super(tagNotFound);
    }
}
