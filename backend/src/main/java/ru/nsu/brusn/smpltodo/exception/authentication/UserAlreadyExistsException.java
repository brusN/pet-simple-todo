package ru.nsu.brusn.smpltodo.exception.authentication;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class UserAlreadyExistsException extends ApiException {

    public UserAlreadyExistsException(String message, TError errorType) {
        super(errorType, message);
    }

    public UserAlreadyExistsException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
