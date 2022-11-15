package ru.nsu.brusn.smpltodo.exception.folder;

import ru.nsu.brusn.smpltodo.exception.ApiException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

public class FolderNotFoundException extends ApiException {

    public FolderNotFoundException(String message, TError errorType) {
        super(errorType, message);
    }

    public FolderNotFoundException(String message, Throwable cause, TError errorType) {
        super(message, cause, errorType);
    }
}
