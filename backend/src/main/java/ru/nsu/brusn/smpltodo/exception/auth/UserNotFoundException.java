package ru.nsu.brusn.smpltodo.exception.auth;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}