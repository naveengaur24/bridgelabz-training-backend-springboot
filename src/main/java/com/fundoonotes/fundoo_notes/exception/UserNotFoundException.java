package com.fundoonotes.fundoo_notes.exception;


// in runtime exception try catch is optional because - compile error does not come..
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message); // parent ko message paas krne ke lie...like - ex.getMessage()
    }
}
