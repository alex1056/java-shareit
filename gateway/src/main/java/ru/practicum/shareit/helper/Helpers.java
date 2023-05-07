package ru.practicum.shareit.helper;

import ru.practicum.shareit.exceptions.BadRequestException;

import java.time.LocalDateTime;

public class Helpers {

    public static void checkDatesValidity(LocalDateTime start, LocalDateTime end) {
        if (start.isEqual(end) || start.isAfter(end) || start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now()))
            throw new BadRequestException("Ошибка валидации: некорректные даты start и/или end");
    }
}
