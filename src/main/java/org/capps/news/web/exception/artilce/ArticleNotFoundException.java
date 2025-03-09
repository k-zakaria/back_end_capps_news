package org.capps.news.web.exception.artilce;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String articleNotFound) {
        super(articleNotFound);
    }
}
