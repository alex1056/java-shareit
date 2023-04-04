package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exceptions.BadRequestException;

public enum BookingSearchStatus {
    ALL, PAST, FUTURE, WAITING, REJECTED, CURRENT;

    public static BookingSearchStatus getStatus(String text) {
        try {
            return BookingSearchStatus.valueOf(text.toUpperCase());
        } catch (RuntimeException error) {
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
