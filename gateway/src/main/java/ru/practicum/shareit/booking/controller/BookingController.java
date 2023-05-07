package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingFromFrontDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.helper.Helpers;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestHeader(HEADER) Long userId,
                                                 @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(defaultValue = "100") @Min(1) Integer size,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        BookingSearchStatus.getStatus(state);
        return bookingClient.getAllBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(
            @Validated @RequestBody BookingFromFrontDto bookingFromFrontDto,
            @RequestHeader(HEADER) Long userId) {
        Helpers.checkDatesValidity(bookingFromFrontDto.getStart(), bookingFromFrontDto.getEnd());
        return bookingClient.addBooking(userId, bookingFromFrontDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@PathVariable Long bookingId,
                                              @RequestHeader(HEADER) Long userId) {
        return bookingClient.findBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
                                                 @RequestHeader(HEADER) Long userId,
                                                 @RequestParam Boolean approved) {
        return bookingClient.findBookingForApprove(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> searchItems(@RequestHeader(HEADER) Long userId,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "100") @Min(1) Integer size,
                                              @RequestParam(defaultValue = "ALL") String state) {
        BookingSearchStatus.getStatus(state);
        return bookingClient.findBookingsByOwner(userId, state, from, size);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Object> deleteBooking(@PathVariable Long bookingId,
                                                @RequestHeader(HEADER) Long userId) {
        return bookingClient.deleteBooking(userId, bookingId);
    }
}
