package com.everett.exceptions.checkedExceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id) {
        super();
    }
    public UserNotFoundException(String email) {
        super();
    }
}
