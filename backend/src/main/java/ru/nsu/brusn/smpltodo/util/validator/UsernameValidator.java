package ru.nsu.brusn.smpltodo.util.validator;

import org.springframework.stereotype.Component;
import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;

import java.util.regex.Pattern;

@Component
public class UsernameValidator implements IValidator<String> {
    private static final String USERNAME_PATTERN = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    @Override
    public void validateData(String object) throws DataValidationException {
        if (!Pattern.matches(USERNAME_PATTERN, object)) {
            throw new DataValidationException("Username " + object + " doesn't match the pattern");
        }
    }
}
