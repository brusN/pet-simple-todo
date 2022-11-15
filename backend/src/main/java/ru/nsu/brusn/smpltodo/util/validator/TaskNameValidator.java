package ru.nsu.brusn.smpltodo.util.validator;

import org.springframework.stereotype.Component;
import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;

import java.util.regex.Pattern;

@Component
public class TaskNameValidator implements IValidator<String> {
    private static final String TASK_NAME_PATTERN = ".+";

    @Override
    public void validateData(String object) throws DataValidationException {
        if (!Pattern.matches(TASK_NAME_PATTERN, object)) {
            throw new DataValidationException("Task name " + object + " doesn't match the pattern", TError.BAD_REQUEST);
        }
    }
}
