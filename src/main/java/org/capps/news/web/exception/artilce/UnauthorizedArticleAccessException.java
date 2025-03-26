package org.capps.news.web.exception.artilce;

public class UnauthorizedArticleAccessException extends RuntimeException {
    public UnauthorizedArticleAccessException(String s) {
        super(s);
    }
}
