package ru.nsu.brusn.smpltodo.exception.auth;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists(String msg) {
        super(msg);
    }
}
