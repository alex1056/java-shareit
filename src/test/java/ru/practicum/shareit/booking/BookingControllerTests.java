package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingFromFrontDto;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = BookingController.class)
class BookingControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto1 = new UserDto(
            1L,
            "user1@mail.ru",
            "Пользователь 1");

    ItemDto itemDto1 = new ItemDto(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null
    );

    BookingFromFrontDto bookingFromFrontDto0 = new BookingFromFrontDto(
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            null,
            1L,
            BookingStatus.WAITING
    );

    BookingToFrontDto bookingToFrontDto1 = new BookingToFrontDto(
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:10"),
            itemDto1,
            userDto1,
            1L,
            BookingStatus.WAITING,
            null
    );

    @Test
    void testAdd() throws Exception {

        BookingFromFrontDto bookingFromFrontDto = new BookingFromFrontDto(
                1L,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(100),
                1L,
                1L,
                BookingStatus.WAITING
        );

        when(bookingService.saveBooking(1L, bookingFromFrontDto))
                .thenReturn(bookingToFrontDto1);

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(bookingFromFrontDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingToFrontDto1.getId()));

        bookingFromFrontDto = new BookingFromFrontDto(
                1L,
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now().plusSeconds(100),
                1L,
                1L,
                BookingStatus.WAITING
        );

        when(bookingService.saveBooking(1L, bookingFromFrontDto))
                .thenReturn(bookingToFrontDto1);

        BookingFromFrontDto finalBookingFromFrontDto = bookingFromFrontDto;
        Assertions.assertThatThrownBy(() ->
                        mvc.perform(post("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(mapper.writeValueAsString(finalBookingFromFrontDto))
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON)))
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("Ошибка валидации");

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(bookingFromFrontDto0))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindBooking() throws Exception {

        when(bookingService.findBooking(1L, 1L))
                .thenReturn(bookingToFrontDto1);

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingToFrontDto1.getId()));

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAll() throws Exception {

        when(bookingService.getAllBookings(
                1L, BookingSearchStatus.CURRENT, 0, 100))
                .thenReturn(List.of(bookingToFrontDto1));

        mvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(100))
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists());


        when(bookingService.getAllBookings(
                1L, BookingSearchStatus.CURRENT, 0, 100))
                .thenReturn(List.of(bookingToFrontDto1));
        Assertions.assertThatThrownBy(() ->
                        mvc.perform(get("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .param("from", String.valueOf(0))
                                .param("size", String.valueOf(100))
                                .param("state", "WRONG_STATUS")
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON)))
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    void testApproveBooking() throws Exception {

        when(bookingService.findBookingForApprove(
                1L, 1L, true))
                .thenReturn(bookingToFrontDto1);

        mvc.perform(patch("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingToFrontDto1.getId()));

        mvc.perform(patch("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tesSearchItems() throws Exception {

        when(bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.CURRENT, 0, 100))
                .thenReturn(List.of(bookingToFrontDto1));

        mvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(100))
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists());

        when(bookingService.getAllBookings(
                1L, BookingSearchStatus.CURRENT, 0, 100))
                .thenReturn(List.of(bookingToFrontDto1));
        Assertions.assertThatThrownBy(() ->
                        mvc.perform(get("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .param("from", String.valueOf(0))
                                .param("size", String.valueOf(100))
                                .param("state", "WRONG_STATUS")
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON)))
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    void testDeleteBooking() throws Exception {

        when(bookingService.findBooking(1L, 1L))
                .thenReturn(bookingToFrontDto1);

        mvc.perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}