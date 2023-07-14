package com.bigdecimal.clasnapp.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException npex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Object is null. [" + npex.getCause() + "]");
    }

    // @ExceptionHandler(EntityNotFoundException.class)
    // public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException enfex) {
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found. [" + enfex.getCause() + "]");
    // }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException nfex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found. [" + nfex.getMessage() + "]");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException isex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Request body is wrongly formed or missing a property. [" + isex.getMessage() + "]");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException iaex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Argument is illegal. [" + iaex.getMessage() + "]");
    }
    
    @ExceptionHandler(InvalidUserDetailsException.class)
    public ResponseEntity<?> handleInvalidUserDetailsException(InvalidUserDetailsException iudex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Signin failed. [" + iudex.getMessage() + "]");
    }
}
