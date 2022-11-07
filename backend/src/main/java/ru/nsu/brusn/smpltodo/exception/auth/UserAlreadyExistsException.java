package ru.nsu.brusn.smpltodo.exception.auth;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
