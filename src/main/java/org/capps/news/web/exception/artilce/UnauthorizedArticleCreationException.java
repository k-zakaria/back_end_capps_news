package org.capps.news.web.exception.artilce;

public class UnauthorizedArticleCreationException extends RuntimeException {
    public UnauthorizedArticleCreationException(String s) {
        super(s);
    }
}
