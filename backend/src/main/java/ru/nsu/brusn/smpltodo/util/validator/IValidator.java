package ru.nsu.brusn.smpltodo.util.validator;

import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;

public interface IValidator<T> {
    void validateData(T object) throws DataValidationException;
}
