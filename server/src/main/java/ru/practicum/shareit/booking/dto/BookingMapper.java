package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingToFrontDto toBookingToFrontDto(Booking booking) {
        if (booking == null) return null;
        return new BookingToFrontDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getBooker() == null ? null : booking.getBooker().getId(),
                booking.getStatus(),
                booking.getState()
        );
    }

    public static List<BookingToFrontDto> toBookingToFrontDto(Iterable<Booking> bookings) {
        List<BookingToFrontDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingToFrontDto(booking));
        }
        return result;
    }
}
