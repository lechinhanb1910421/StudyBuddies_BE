package com.everett.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id) {
        super();
    }
    public UserNotFoundException(String email) {
        super();
    }
}
