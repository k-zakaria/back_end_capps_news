package org.capps.news.web.exception.category;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryNotFound) {
        super(categoryNotFound);
    }
}
