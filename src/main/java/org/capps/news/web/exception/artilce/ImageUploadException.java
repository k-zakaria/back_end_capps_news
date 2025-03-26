package org.capps.news.web.exception.artilce;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String s, Exception e) {
        super(s,e);
    }
}
