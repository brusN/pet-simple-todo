package ru.nsu.brusn.smpltodo.util.validator;

import org.springframework.stereotype.Component;
import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

import java.util.regex.Pattern;

@Component
public class PasswordValidator implements IValidator<String> {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    @Override
    public void validateData(String object) throws DataValidationException {
        if (!Pattern.matches(PASSWORD_PATTERN, object)) {
            throw new DataValidationException("Password " + object + " doesn't match the pattern", TError.BAD_REQUEST);
        }
    }
}
