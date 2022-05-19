package com.bezkoder.spring.data.mongodb.authservice.exception;


public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
