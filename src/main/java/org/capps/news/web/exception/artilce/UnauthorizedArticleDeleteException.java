package org.capps.news.web.exception.artilce;

public class UnauthorizedArticleDeleteException extends RuntimeException {
    public UnauthorizedArticleDeleteException(String s) {
        super(s);
    }
}
