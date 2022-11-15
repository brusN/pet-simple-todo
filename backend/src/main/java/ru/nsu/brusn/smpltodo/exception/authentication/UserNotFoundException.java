package ru.nsu.brusn.smpltodo.exception.authentication;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(String message, TError errorType) {
        super(errorType, message);
    }

    public UserNotFoundException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}