package ru.practicum.shareit.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SizeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSize {
    String message() default "Параметр 'size' должен быть больше нуля";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
