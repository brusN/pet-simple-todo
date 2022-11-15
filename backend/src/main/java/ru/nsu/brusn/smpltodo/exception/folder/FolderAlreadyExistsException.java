package ru.nsu.brusn.smpltodo.exception.folder;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class FolderAlreadyExistsException extends ApiException {

    public FolderAlreadyExistsException(String message, TError errorType) {
        super(errorType, message);
    }

    public FolderAlreadyExistsException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
