package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingFromFrontDto;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.helper.Helpers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    CommentRepository mockCommentRepository;
    @Mock
    UserService mockUserService;
    BookingService bookingService;

    BookingFromFrontDto bookingFromFrontDto = new BookingFromFrontDto(
            null,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            1L,
            1L,
            BookingStatus.WAITING
    );

    private final User user1 = new User(
            1L,
            "user1@mail.ru",
            "Пользователь 1");

    private final UserDto userDto1 = new UserDto(
            1L,
            "user1@mail.ru",
            "Пользователь 1");

    ItemDto itemDto2 = new ItemDto(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            null,
            null,
            null);

    Item item1 = new Item(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null,
            null,
            null
    );

    Item item2 = new Item(
            2L,
            "Вещь 2",
            "Описание вещи 2",
            false,
            1L,
            null,
            null,
            null,
            null
    );

    private final UserDto userDto2 = new UserDto(
            2L,
            "user2@mail.ru",
            "Пользователь 2");

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(
                mockBookingRepository, mockUserService, mockItemRepository
        );
    }

    Booking booking = new Booking(
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.now(),
            item1,
            user1,
            BookingStatus.WAITING,
            null
    );

    Booking bookingApproved = new Booking(
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.now(),
            item1,
            user1,
            BookingStatus.WAITING,
            BookingState.APPROVED
    );

    @Test
    void testSaveBooking() {

        when(mockUserService.findUserById(2L))
                .thenReturn(userDto2);

        when(mockItemRepository.findById(any()))
                .thenReturn(Optional.ofNullable(item1));

        when(mockBookingRepository.save(any()))
                .thenReturn(booking);

        BookingToFrontDto bookingToFrontDto = bookingService.saveBooking(2L, bookingFromFrontDto);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(2L);

        Assertions.assertEquals(1, bookingToFrontDto.getId());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getStart().toString());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getEnd().toString());
        Assertions.assertEquals(BookingStatus.WAITING, bookingToFrontDto.getStatus());
        Assertions.assertNull(bookingToFrontDto.getState());
        Assertions.assertEquals(1L, bookingToFrontDto.getItem().getId());
        Assertions.assertEquals("Вещь 1", bookingToFrontDto.getItem().getName());
        Assertions.assertEquals("Описание вещи 1", bookingToFrontDto.getItem().getDescription());
        Assertions.assertTrue(bookingToFrontDto.getItem().isAvailable());
        Assertions.assertEquals(1L, bookingToFrontDto.getBooker().getId());
        Assertions.assertEquals("user1@mail.ru", bookingToFrontDto.getBooker().getEmail());

        when(mockItemRepository.findById(any()))
                .thenReturn(Optional.ofNullable(null));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.saveBooking(2L, bookingFromFrontDto));

        when(mockUserService.findUserById(2L))
                .thenReturn(userDto2);

        when(mockItemRepository.findById(any()))
                .thenReturn(Optional.ofNullable(item2));

        final BadRequestException exception2 = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.saveBooking(2L, bookingFromFrontDto));

        when(mockUserService.findUserById(1L))
                .thenReturn(userDto1);

        when(mockItemRepository.findById(any()))
                .thenReturn(Optional.ofNullable(item1));

        final NotFoundException exception3 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.saveBooking(1L, bookingFromFrontDto));
    }

    @Test
    void testFindBooking() {

        when(mockUserService.findUserById(1L))
                .thenReturn(userDto1);

        when(mockBookingRepository.findBooking(any(), any()))
                .thenReturn(Optional.ofNullable(booking));

        BookingToFrontDto bookingToFrontDto = bookingService.findBooking(1L, 1L);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, bookingToFrontDto.getId());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getStart().toString());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getEnd().toString());
        Assertions.assertEquals(BookingStatus.WAITING, bookingToFrontDto.getStatus());
        Assertions.assertNull(bookingToFrontDto.getState());
        Assertions.assertEquals(1L, bookingToFrontDto.getItem().getId());
        Assertions.assertEquals("Вещь 1", bookingToFrontDto.getItem().getName());
        Assertions.assertEquals("Описание вещи 1", bookingToFrontDto.getItem().getDescription());
        Assertions.assertTrue(bookingToFrontDto.getItem().isAvailable());
        Assertions.assertEquals(1L, bookingToFrontDto.getBooker().getId());
        Assertions.assertEquals("user1@mail.ru", bookingToFrontDto.getBooker().getEmail());

        when(mockBookingRepository.findBooking(any(), any()))
                .thenReturn(Optional.ofNullable(null));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.findBooking(1L, 1L));
    }

    @Test
    void testFindBookingsByOwner() {

        when(mockUserService.findUserById(1L))
                .thenReturn(userDto1);

        Pageable page = PageRequest.of(Helpers.getPageNumber(0, 100), 100);
        when(mockBookingRepository.findBookingsByOwner(1L, page))
                .thenReturn(List.of(booking, booking));

        List<BookingToFrontDto> bookingToFrontDtoList = bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.ALL, 0, 100);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(2, bookingToFrontDtoList.size());

        Booking booking1Current = new Booking(
                1L,
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now(),
                item1,
                user1,
                BookingStatus.WAITING,
                null
        );

        Booking booking2Past = new Booking(
                1L,
                LocalDateTime.now().minusSeconds(20),
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now(),
                item1,
                user1,
                BookingStatus.WAITING,
                null
        );

        Booking booking2Future = new Booking(
                1L,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now(),
                item1,
                user1,
                BookingStatus.WAITING,
                null
        );

        Booking booking2Rejected = new Booking(
                1L,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now(),
                item1,
                user1,
                BookingStatus.REJECTED,
                null
        );

        page = PageRequest.of(Helpers.getPageNumber(0, 100), 100);
        when(mockBookingRepository.findBookingsByOwner(1L, page))
                .thenReturn(List.of(booking1Current, booking1Current, booking2Past, booking2Future));


        bookingToFrontDtoList = bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.CURRENT, 0, 100);
        Assertions.assertEquals(2, bookingToFrontDtoList.size());

        when(mockBookingRepository.findBookingsByOwner(1L, page))
                .thenReturn(List.of(booking1Current, booking2Past, booking2Past));

        bookingToFrontDtoList = bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.PAST, 0, 100);
        Assertions.assertEquals(2, bookingToFrontDtoList.size());

        when(mockBookingRepository.findBookingsByOwner(1L, page))
                .thenReturn(List.of(booking2Future, booking2Future, booking1Current, booking2Past, booking2Past));

        bookingToFrontDtoList = bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.FUTURE, 0, 100);
        Assertions.assertEquals(2, bookingToFrontDtoList.size());

        when(mockBookingRepository.findBookingsByOwner(1L, page))
                .thenReturn(List.of(booking2Future, booking2Future, booking2Rejected,
                        booking1Current, booking2Past, booking2Past));

        bookingToFrontDtoList = bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.WAITING, 0, 100);
        Assertions.assertEquals(5, bookingToFrontDtoList.size());

        when(mockBookingRepository.findBookingsByOwner(1L, page))
                .thenReturn(List.of(booking2Future, booking2Future, booking2Rejected, booking1Current, booking2Past, booking2Past, booking2Rejected));

        bookingToFrontDtoList = bookingService.findBookingsByOwner(
                1L, BookingSearchStatus.REJECTED, 0, 100);
        Assertions.assertEquals(2, bookingToFrontDtoList.size());

    }

    @Test
    void testFindBookingForApprove() {

        when(mockUserService.findUserById(1L))
                .thenReturn(userDto1);

        when(mockBookingRepository.findBookingForApprove(any(), any()))
                .thenReturn(Optional.ofNullable(booking));

        when(mockBookingRepository.save(any()))
                .thenReturn(booking);


        BookingToFrontDto bookingToFrontDto = bookingService.findBookingForApprove(1L, 1L, true);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, bookingToFrontDto.getId());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getStart().toString());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getEnd().toString());
        Assertions.assertEquals(BookingStatus.APPROVED, bookingToFrontDto.getStatus());
        Assertions.assertEquals(BookingState.APPROVED, bookingToFrontDto.getState());
        Assertions.assertEquals(1L, bookingToFrontDto.getItem().getId());
        Assertions.assertEquals("Вещь 1", bookingToFrontDto.getItem().getName());
        Assertions.assertEquals("Описание вещи 1", bookingToFrontDto.getItem().getDescription());
        Assertions.assertTrue(bookingToFrontDto.getItem().isAvailable());
        Assertions.assertEquals(1L, bookingToFrontDto.getBooker().getId());
        Assertions.assertEquals("user1@mail.ru", bookingToFrontDto.getBooker().getEmail());

        when(mockBookingRepository.findBookingForApprove(any(), any()))
                .thenReturn(Optional.ofNullable(booking));

        bookingToFrontDto = bookingService.findBookingForApprove(1L, 1L, false);
        Assertions.assertEquals(1, bookingToFrontDto.getId());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getStart().toString());
        Assertions.assertEquals("2023-04-22T19:12:08", bookingToFrontDto.getEnd().toString());
        Assertions.assertEquals(BookingStatus.REJECTED, bookingToFrontDto.getStatus());
        Assertions.assertEquals(BookingState.REJECTED, bookingToFrontDto.getState());

        when(mockBookingRepository.findBookingForApprove(any(), any()))
                .thenReturn(Optional.ofNullable(bookingApproved));

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.findBookingForApprove(1L, 1L, true));

        when(mockBookingRepository.findBookingForApprove(any(), any()))
                .thenReturn(Optional.ofNullable(null));

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.findBookingForApprove(1L, 1L, true));
    }

    @Test
    void testDeleteBooking() {

        when(mockUserService.findUserById(1L))
                .thenReturn(userDto1);

        when(mockBookingRepository.findBookingByIdAndBookerId(1L, 1L))
                .thenReturn(Optional.ofNullable(booking));

        bookingService.deleteBooking(1L, 1L);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Mockito.verify(mockBookingRepository, Mockito.times(1))
                .deleteById(1L);

        when(mockBookingRepository.findBookingByIdAndBookerId(any(), any()))
                .thenReturn(Optional.ofNullable(null));

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.deleteBooking(1L, 1L));
    }
}