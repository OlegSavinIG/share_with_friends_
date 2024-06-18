package ru.practicum.shareit.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

public class SizeValidator implements ConstraintValidator<ValidSize, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null || value <= 0) {
            throw new ValidationException("Неправильное значение size: " + value);
        }
        return true;
    }
}
