package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookItemRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidBookItemRequestDto() {
        BookItemRequestDto dto = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidBookItemRequestDtoWithPastStart() {
        BookItemRequestDto dto = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("must be a date in the present or in the future", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidBookItemRequestDtoWithPastEnd() {
        BookItemRequestDto dto = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("must be a future date", violations.iterator().next().getMessage());
    }
}
