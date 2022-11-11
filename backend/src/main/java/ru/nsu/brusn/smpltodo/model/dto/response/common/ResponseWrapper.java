package ru.nsu.brusn.smpltodo.model.dto.response.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapper<T> {
    private T data;

    public ResponseWrapper(T data) {
        this.data = data;
    }

    public static ResponseWrapper<MessageResponse> okResponse(String message) {
        return new ResponseWrapper<>(new MessageResponse(message));
    }

    public static ResponseWrapper<ErrorResponse> errorResponse(TError errorType, String message) {
        return new ResponseWrapper<>(new ErrorResponse(errorType.getDescription(), message));
    }
}

