package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingFromFrontDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserService userService;

    private final ItemRepository itemRepository;


    @Override
    public List<BookingToFrontDto> getAllBookings(Long userId, BookingSearchStatus searchStatus) {
        userService.findUserById(userId);
        List<Booking> bookings = repository.findBookingByBookerIdOrderByStartDesc(userId);
        return filterBookingsByStatus(bookings, searchStatus);
    }

    @Override
    public BookingToFrontDto saveBooking(Long userId, BookingFromFrontDto bookingFromFrontDto) {
        UserDto booker = userService.findUserById(userId);
        Optional<Item> itemOptional = itemRepository.findById(bookingFromFrontDto.getItemId());
        itemOptional.orElseThrow(() -> new NotFoundException("предмет с id=" + bookingFromFrontDto.getItemId()));
        if (!itemOptional.get().getAvailable()) {
            throw new BadRequestException("предмет с id=" + bookingFromFrontDto.getItemId() + " не доступен для заказа");
        }
        if (bookingFromFrontDto.getItemId().equals(userId)) {
            throw new NotFoundException("влалец не может заказать сам у семя предмет");
        }
        // проверки закончились
        Booking booking = new Booking(null,
                bookingFromFrontDto.getStart(),
                bookingFromFrontDto.getEnd(),
                LocalDateTime.now(),
                itemOptional.get(),
                UserMapper.toUser(booker),
                BookingStatus.WAITING,
                null
        );

        Booking bookingSaved = repository.save(booking);
        return BookingMapper.toBookingToFrontDto(bookingSaved);
    }

    @Override
    public BookingToFrontDto findBooking(Long bookingId, Long userId) {
        userService.findUserById(userId);
        Optional<Booking> bookingOpt = repository.findBooking(bookingId, userId);
        bookingOpt.orElseThrow(() -> new NotFoundException("booking c id= " + bookingId));
        return BookingMapper.toBookingToFrontDto(bookingOpt.get());
    }

    @Override
    public List<BookingToFrontDto> findBookingsByOwner(Long userId, BookingSearchStatus status) {
        userService.findUserById(userId);
        List<Booking> bookings = repository.findBookingsByOwner(userId);
        return filterBookingsByStatus(bookings, status);
    }

    @Override
    public BookingToFrontDto findBookingForApprove(Long bookingId, Long userId, Boolean approved) {
        userService.findUserById(userId);
        Optional<Booking> bookingOpt = repository.findBookingForApprove(bookingId, userId);
        bookingOpt.orElseThrow(() -> new NotFoundException("booking c id= " + bookingId));
        Booking booking = bookingOpt.get();


        if (approved && booking.getState() != null && booking.getState().equals(BookingState.APPROVED)) {
            throw new BadRequestException("подтверждение уже имеется");
        }
        if (approved) {
            booking.setState(BookingState.APPROVED);
            booking.setStatus(BookingStatus.APPROVED);
            return BookingMapper.toBookingToFrontDto(repository.save(booking));
        }
        booking.setState(BookingState.REJECTED);
        booking.setStatus(BookingStatus.REJECTED);
        return BookingMapper.toBookingToFrontDto(repository.save(booking));
    }

    @Override
    public void deleteBooking(Long id, Long userId) {
        userService.findUserById(userId);
        Optional<Booking> bookingOpt = repository.findBookingByIdAndBookerId(id, userId);
        bookingOpt.orElseThrow(() -> new NotFoundException("booking c id= " + id));
        repository.deleteById(id);
    }

    private List<BookingToFrontDto> filterBookingsByStatus(List<Booking> bookings, BookingSearchStatus status) {
        List<Booking> filteredBookings = new ArrayList<>();
        switch (status) {
            case ALL:
                return BookingMapper.toBookingToFrontDto(bookings);
            case PAST:
                filteredBookings = bookings.stream().filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                filteredBookings = bookings.stream()
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()) && booking.getStart().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                filteredBookings = bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case WAITING:
                filteredBookings = bookings.stream()
                        .filter(booking -> BookingStatus.valueOf(booking.getStatus().toString()).equals(BookingStatus.WAITING))
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                filteredBookings = bookings.stream()
                        .filter(booking -> BookingStatus.valueOf(booking.getStatus().toString()).equals(BookingStatus.REJECTED))
                        .collect(Collectors.toList());
                break;
        }
        return BookingMapper.toBookingToFrontDto(filteredBookings);
    }

}