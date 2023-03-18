package ru.practicum.shareit.exceptions;

public class SmthAlreadyExistException extends RuntimeException {
    public SmthAlreadyExistException(String message) {
        super(message);
    }
}
