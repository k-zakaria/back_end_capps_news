package org.capps.news.web.exception;


import io.jsonwebtoken.ExpiredJwtException;
import org.capps.news.web.exception.artilce.*;
import org.capps.news.web.exception.category.CategoryNotFoundException;
import org.capps.news.web.exception.tag.TagNotFoundException;
import org.capps.news.web.exception.user.UnauthorizedRoleUpdateException;
import org.capps.news.web.exception.user.UserNameAlreadyExistsException;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.capps.news.web.exception.user.UsernameOrPasswordInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.management.relation.RoleNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * --------------------------------------- User Exceptions
     */
    //  ---------------  UserNameAlreadyExistsException
    @ExceptionHandler(UserNameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleUserNameAlreadyExistsException(
            UserNameAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());
        responseBody.put("status", HttpStatus.CONFLICT.value());
        responseBody.put("error", "Conflict");
        responseBody.put("message", ex.getMessage());
        responseBody.put("path", request.getDescription(false).replace("uri=", "")); // Request path

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    //  ---------------  UsernameOrPasswordInvalidException
    @ExceptionHandler(UsernameOrPasswordInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleUsernameOrPasswordInvalidException(
            UsernameOrPasswordInvalidException ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("error", "Unauthorized");
        responseBody.put("message", ex.getMessage());
        responseBody.put("path", request.getDescription(false).replace("uri=", "")); // Request path

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    //  ---------------  RoleNotFoundException
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleRoleNotFoundException(
            RoleNotFoundException ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("error", "Not Found");
        responseBody.put("message", ex.getMessage());
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    //  ---------------  UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Bad Request");
        responseBody.put("message", ex.getMessage());
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    /**
     * --------------------------------------- Global Exceptions
     */

    //  ---------------  FieldCannotBeNullException
    @ExceptionHandler(FieldCannotBeNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleFieldCannotBeNullException(
            FieldCannotBeNullException ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Bad Request");
        responseBody.put("message", ex.getMessage());
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }


    //ExpiredJwtException
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleExpiredJwtException(WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("error", "Unauthorized");
        responseBody.put("message", "Access token expired");
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    //// Article Exeption
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<String> articleNotFound(ArticleNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedArticleUpdateException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedArticleUpdate(
            UnauthorizedArticleUpdateException ex,
            WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedArticleDeleteException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedArticleDelete(
            UnauthorizedArticleDeleteException ex,
            WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    /// Category Exeption
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> categoryNotFound(CategoryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //// Tag Exeption

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> tagNotFound(TagNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedRoleUpdateException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedRoleUpdate(
            UnauthorizedRoleUpdateException ex,
            WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedArticleAccessException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedArticleAccess(
            UnauthorizedArticleAccessException ex,
            WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedPublicationException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedPublication(
            UnauthorizedPublicationException ex,
            WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}






