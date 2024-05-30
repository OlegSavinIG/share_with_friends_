package ru.practicum.shareit.annotation;

import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStatusValidator implements ConstraintValidator<ValidBookingStatus, String> {

    @Override
    public void initialize(ValidBookingStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        if (status == null) {
            return false;
        }
        try {
            BookingStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
