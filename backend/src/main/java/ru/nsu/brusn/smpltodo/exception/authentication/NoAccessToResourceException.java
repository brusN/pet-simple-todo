package ru.nsu.brusn.smpltodo.exception.authentication;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class NoAccessToResourceException extends ApiException {
    public NoAccessToResourceException(TError errorType, String message) {
        super(errorType, message);
    }

    public NoAccessToResourceException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
