package ru.nsu.brusn.smpltodo.exception.task;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class TaskNotFoundException extends ApiException {
    public TaskNotFoundException(String message, TError errorType) {
        super(errorType, message);
    }

    public TaskNotFoundException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
