package ru.nsu.brusn.smpltodo.exception.validation;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class DataValidationException extends ApiException {

    public DataValidationException(String message, TError errorType) {
        super(errorType, message);
    }

    public DataValidationException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
