package ru.nsu.brusn.smpltodo.exception;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private TError errorType;

    public ApiException(String message, TError errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ApiException(String message, Throwable cause, TError errorType) {
        super(message, cause);
        this.errorType = errorType;
    }
}
