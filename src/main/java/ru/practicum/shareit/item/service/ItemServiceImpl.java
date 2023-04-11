package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.helper.Helpers;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final BookingRepository bookingRepository;
    private final UserService userService;

    private final CommentRepository commentRepository;

    @Override
    public List<ItemCommentsDto> getAll(Long userId) {
        userService.findUserById(userId);
        List<Item> itemList = repository.findItemByOwnerId(userId);

        for (Item item : itemList) {
            Optional<Booking> bookingOptLast = bookingRepository.findNearestBookingByEndDate(item.getId(), LocalDateTime.now());

            if (bookingOptLast.isPresent()) {
                item.setLastBooking(BookingMapper.toBookingToFrontDto(bookingOptLast.get()));
            }
            Optional<Booking> bookingOptNext = bookingRepository.findNearestBookingByStartDate(item.getId(), LocalDateTime.now());
            if (bookingOptNext.isPresent()) {
                item.setNextBooking(BookingMapper.toBookingToFrontDto(bookingOptNext.get()));
            }
        }
        return ItemMapper.toItemDtoComments(itemList);
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.findUserById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(repository.save(item));
    }

    @Override
    public ItemDto findById(Long userId, Long id) {
        userService.findUserById(userId);
        Optional<Item> item = repository.findItemByOwnerIdAndId(userId, id);
        item.orElseThrow(() -> new NotFoundException("предмет с id=" + id));
        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public ItemCommentsDto findByIdAnyUser(Long userId, Long id) {
        Optional<Item> itemOpt = repository.findById(id);
        itemOpt.orElseThrow(() -> new NotFoundException("предмет с id=" + id));
        Item item = itemOpt.get();
        if (item.getOwnerId().equals(userId)) {
            Optional<Booking> bookingOptLast = bookingRepository.findNearestBookingByEndDate(item.getId(), LocalDateTime.now());
            if (bookingOptLast.isPresent() && isRealBooking(bookingOptLast.get())) {
                item.setLastBooking(BookingMapper.toBookingToFrontDto(bookingOptLast.get()));
            }
            Optional<Booking> bookingOptNext = bookingRepository.findNearestBookingByStartDate(item.getId(), LocalDateTime.now());
            if (bookingOptNext.isPresent() && isRealBooking(bookingOptNext.get())) {
                item.setNextBooking(BookingMapper.toBookingToFrontDto(bookingOptNext.get()));
            }
            Optional<Booking> bookingOptCurrent = bookingRepository.findNearestBookingByCurrentDate(item.getId(), LocalDateTime.now());
            if (bookingOptCurrent.isPresent() && isRealBooking(bookingOptCurrent.get())) {
                item.setLastBooking(BookingMapper.toBookingToFrontDto(bookingOptCurrent.get()));
            }
        }
        return ItemMapper.toItemDtoComments(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userService.findUserById(userId);
        Item item = ItemMapper.toItem(itemDto);
        Optional<Item> itemFound = repository.findItemByOwnerIdAndId(userId, itemId);
        itemFound.orElseThrow(() -> new NotFoundException("предмет с id=" + itemId));
        Item itemToSave = Helpers.composeFieldsForUpdateItem(itemFound.get(), item);
        return ItemMapper.toItemDto(repository.save(itemToSave));
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> itemList = repository.search(text);
        return ItemMapper.toItemDto(itemList);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentNewDto commentNewDto) {
        UserDto userDto = userService.findUserById(userId);
        findByIdAnyUser(userId, itemId);
        List<Booking> bookingsList = bookingRepository.findBookingByItemIdAndBookerId(itemId, userId);
        if (bookingsList.size() == 0) {
            throw new BadRequestException("пользователь не делал бронирование");
        }
        for (Booking booking : bookingsList) {
            if (booking.getStatus().equals(BookingStatus.REJECTED) || booking.getState().equals(BookingState.REJECTED)) {
                throw new BadRequestException("хотя бы одно бронирование было отклонено");
            }
        }
        Optional<Booking> bookingOptLast = bookingRepository.findNearestBookingByEndDateAndUserId(itemId, userId, LocalDateTime.now());
        Optional<Booking> bookingOptNext = bookingRepository.findNearestBookingByStartDateAndUserId(itemId, userId, LocalDateTime.now());
        if (bookingOptLast.isEmpty() && bookingOptNext.isPresent()) {
            throw new BadRequestException("нельзя откомментировать будущее бронирование и предмет");
        }

        Comment comment = new Comment(
                null,
                commentNewDto.getText(),
                itemId,
                UserMapper.toUser(userDto),
                LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }

    private Boolean isRealBooking(Booking booking) {
        if (booking == null || booking.getStatus() == null || booking.getState() == null) return false;
        if (!booking.getState().equals(BookingState.REJECTED)
                &&
                !booking.getStatus().equals(BookingStatus.REJECTED)
                &&
                !booking.getStatus().equals(BookingStatus.CANCELED))
            return true;
        return false;
    }
}