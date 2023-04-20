package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingFromFrontDto;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;

import java.util.List;

public interface BookingService {
    List<BookingToFrontDto> getAllBookings(Long userId, BookingSearchStatus searchStatus, Integer from, Integer size);

    BookingToFrontDto saveBooking(Long userId, BookingFromFrontDto bookingFromFrontDto);

    BookingToFrontDto findBooking(Long bookingId, Long userId);

    List<BookingToFrontDto> findBookingsByOwner(Long userId, BookingSearchStatus status, Integer from, Integer size);

    BookingToFrontDto findBookingForApprove(Long bookingId, Long userId, Boolean approved);

    void deleteBooking(Long id, Long userId);
}