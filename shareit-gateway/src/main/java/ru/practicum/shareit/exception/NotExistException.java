package ru.practicum.shareit.exception;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }

    public NotExistException(String message, Object... args) {
        super(String.format(message, args));
    }
}
