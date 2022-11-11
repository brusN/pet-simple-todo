package ru.nsu.brusn.smpltodo.exception.auth;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class UserAlreadyExistsException extends ApiException {

    public UserAlreadyExistsException(String message, TError errorType) {
        super(message, errorType);
    }

    public UserAlreadyExistsException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
