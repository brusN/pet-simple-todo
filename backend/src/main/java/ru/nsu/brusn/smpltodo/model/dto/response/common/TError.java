package ru.nsu.brusn.smpltodo.model.dto.response.common;

import lombok.Getter;

@Getter
public enum TError {
    API_ERROR("SERVER API ERROR"),
    BAD_REQUEST("BAD REQUEST"),
    NO_ACCESS("NO ACCESS"),
    VALIDATION_ERROR("VALIDATION ERROR");

    private final String description;

    TError(String description) {
        this.description = description;
    }
}
