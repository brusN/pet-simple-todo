package ru.nsu.brusn.smpltodo.exception.other;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class RoleNotFoundException extends ApiException {

    public RoleNotFoundException(String message, TError errorType) {
        super(errorType, message);
    }

    public RoleNotFoundException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
