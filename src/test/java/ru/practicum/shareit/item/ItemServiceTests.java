package ru.practicum.shareit.item;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.helper.Helpers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentFromFrontDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    CommentRepository mockCommentRepository;
    @Mock
    UserService mockUserService;
    ItemService itemService;
    private final User user1 = new User(
            1L,
            "user1@mail.ru",
            "Пользователь 1");
    ItemDto itemDto1 = new ItemDto(
            null,
            "Вещь 1",
            "Описание вещи 1",
            true,
            null,
            null,
            null);

    Item item0ForGetAll = new Item(
            1L,
            "Вещь 0",
            "Описание вещи 0",
            true,
            1L,
            null,
            null,
            null,
            null
    );

    Item item0 = new Item(
            null,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null,
            null,
            null
    );
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

    Booking booking1 = new Booking(
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            item1,
            user1,
            BookingStatus.WAITING,
            BookingState.APPROVED
    );

    Booking booking2 = new Booking(
            2L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            item1,
            user1,
            BookingStatus.CANCELED,
            BookingState.APPROVED
    );

    Booking booking3 = new Booking(
            2L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            item1,
            user1,
            BookingStatus.APPROVED,
            BookingState.APPROVED
    );

    Booking booking4 = new Booking(
            2L,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            item1,
            user1,
            BookingStatus.REJECTED,
            BookingState.REJECTED
    );

    CommentFromFrontDto commentFromFrontDto = new CommentFromFrontDto(
            1L,
            "Комментарий 1"
    );

    ItemCommentDto itemCommentDto1 = new ItemCommentDto(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null,
            null
    );

    private final UserDto userDto = new UserDto(
            1L,
            "user1@mail.ru",
            "Пользователь 1");

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(
                mockItemRepository, mockBookingRepository, mockUserService, mockCommentRepository);
    }

    @Test
    void testCreateItem() {

        when(mockItemRepository.save(item0))
                .thenReturn(item1);

        ItemDto itemDto = itemService.createItem(1L, itemDto1);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals("Вещь 1", itemDto.getName());
        Assertions.assertEquals("Описание вещи 1", itemDto.getDescription());
        Assertions.assertEquals(true, itemDto.isAvailable());
        Assertions.assertNull(itemDto.getRequestId());
        Assertions.assertNull(itemDto.getLastBooking());
        Assertions.assertNull(itemDto.getNextBooking());
    }

    @Test
    void testUpdateItem() {

        when(mockItemRepository.findItemByOwnerIdAndId(1L, 1L))
                .thenReturn(Optional.ofNullable(item1));

        when(mockItemRepository.save(item1))
                .thenReturn(item1);

        ItemDto itemDto = itemService.updateItem(1L, 1L, itemDto1);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals("Вещь 1", itemDto.getName());
        Assertions.assertEquals("Описание вещи 1", itemDto.getDescription());
        Assertions.assertEquals(true, itemDto.isAvailable());
        Assertions.assertNull(itemDto.getRequestId());
        Assertions.assertNull(itemDto.getLastBooking());
        Assertions.assertNull(itemDto.getNextBooking());
    }

    @Test
    void testSearch() {

        PageRequest pageRequest = PageRequest.of(Helpers.getPageNumber(0, 2), 2);

        when(mockItemRepository.search("abc", pageRequest))
                .thenReturn(List.of(item0ForGetAll, item1));

        List<ItemDto> itemDtoList = itemService.search("abc", 0, 2);

        Assertions.assertEquals(2, itemDtoList.size());
        Assertions.assertEquals(1, itemDtoList.get(0).getId());
        Assertions.assertEquals("Вещь 0", itemDtoList.get(0).getName());
        Assertions.assertEquals("Описание вещи 0", itemDtoList.get(0).getDescription());
        Assertions.assertEquals(true, itemDtoList.get(0).isAvailable());
        Assertions.assertNull(itemDtoList.get(0).getRequestId());
        Assertions.assertNull(itemDtoList.get(0).getLastBooking());
        Assertions.assertNull(itemDtoList.get(0).getNextBooking());
    }


    @Test
    void testFindById() {

        when(mockItemRepository.findItemByOwnerIdAndId(1L, 1L))
                .thenReturn(Optional.ofNullable(item1));

        ItemDto itemDto = itemService.findById(1L, 1L);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals("Вещь 1", itemDto.getName());
        Assertions.assertEquals("Описание вещи 1", itemDto.getDescription());
        Assertions.assertEquals(true, itemDto.isAvailable());
        Assertions.assertNull(itemDto.getRequestId());
        Assertions.assertNull(itemDto.getLastBooking());
        Assertions.assertNull(itemDto.getNextBooking());

        when(mockItemRepository.findItemByOwnerIdAndId(1L, 2L))
                .thenThrow(new NotFoundException("предмет с id=2"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.findById(1L, 2L));

        Assertions.assertEquals("предмет с id=2", exception.getMessage());

    }

    @Test
    public void testFindByIdAnyUserFixedInstant() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            var mockLocalDateTime = mock(LocalDateTime.class);
            when(mockLocalDateTime.getSecond()).thenReturn(0);
            mocked.when(LocalDateTime::now).thenReturn(mockLocalDateTime);
            var result = LocalDateTime.now();
            assertThat(result.getSecond()).isEqualTo(0);

            when(mockItemRepository.findById(1L))
                    .thenReturn(Optional.ofNullable(item1));
            when(mockBookingRepository.findNearestBookingByEndDate(1L, LocalDateTime.now()))
                    .thenReturn(Optional.ofNullable(booking1));
            when(mockBookingRepository.findNearestBookingByStartDate(1L, LocalDateTime.now()))
                    .thenReturn(Optional.ofNullable(booking1));
            when(mockBookingRepository.findNearestBookingByCurrentDate(1L, LocalDateTime.now()))
                    .thenReturn(Optional.ofNullable(booking3));

            ItemCommentDto itemCommentDto = itemService.findByIdAnyUser(1L, 1L);
            Assertions.assertEquals(1, itemCommentDto.getId());
            Assertions.assertEquals("Вещь 1", itemCommentDto.getName());
            Assertions.assertEquals("Описание вещи 1", itemCommentDto.getDescription());
            Assertions.assertEquals(true, itemCommentDto.isAvailable());
            Assertions.assertNull(itemCommentDto.getRequestId());
            Assertions.assertNotNull(itemCommentDto.getLastBooking());
            Assertions.assertNotNull(itemCommentDto.getNextBooking());
            Assertions.assertEquals(0, itemCommentDto.getComments().size());

            when(mockUserService.findUserById(1L))
                    .thenReturn(userDto);

            when(mockBookingRepository.findBookingByItemIdAndBookerId(1L, 1L))
                    .thenReturn(List.of(booking1, booking2));

            Comment comment = new Comment(
                    null,
                    commentFromFrontDto.getText(),
                    1L,
                    UserMapper.toUser(userDto),
                    LocalDateTime.now());

            Comment commentSaved = new Comment(
                    1L,
                    commentFromFrontDto.getText(),
                    1L,
                    UserMapper.toUser(userDto),
                    LocalDateTime.now());

            when(mockCommentRepository.save(comment))
                    .thenReturn(commentSaved);

            CommentDto commentDto = itemService.addComment(1L, 1L, commentFromFrontDto);

        }
    }

    @Test
    public void testGetAllFixedInstant() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            var mockLocalDateTime = mock(LocalDateTime.class);
            when(mockLocalDateTime.getSecond()).thenReturn(0);
            mocked.when(LocalDateTime::now).thenReturn(mockLocalDateTime);
            var result = LocalDateTime.now();
            assertThat(result.getSecond()).isEqualTo(0);
            PageRequest pageRequest = PageRequest.of(Helpers.getPageNumber(0, 2), 2);

            when(mockItemRepository.findItemByOwnerId(1L, pageRequest))
                    .thenReturn(List.of(item0ForGetAll, item1));

            when(mockBookingRepository.findNearestBookingByEndDate(1L, LocalDateTime.now()))
                    .thenReturn(Optional.ofNullable(booking1));
            when(mockBookingRepository.findNearestBookingByStartDate(1L, LocalDateTime.now()))
                    .thenReturn(Optional.ofNullable(booking1));

            List<ItemCommentDto> itemCommentDtoList = itemService.getAll(1L, 0, 2);
            Assertions.assertEquals(2, itemCommentDtoList.size());
            Assertions.assertEquals(1, itemCommentDtoList.get(0).getId());
            Assertions.assertEquals("Вещь 0", itemCommentDtoList.get(0).getName());
            Assertions.assertEquals("Описание вещи 0", itemCommentDtoList.get(0).getDescription());
            Assertions.assertEquals(true, itemCommentDtoList.get(0).isAvailable());
            Assertions.assertNull(itemCommentDtoList.get(0).getRequestId());
            Assertions.assertNotNull(itemCommentDtoList.get(0).getLastBooking());
            Assertions.assertNotNull(itemCommentDtoList.get(0).getNextBooking());
            Assertions.assertEquals(0, itemCommentDtoList.get(0).getComments().size());
        }
    }

    @Test
    public void testAddComment() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            var mockLocalDateTime = mock(LocalDateTime.class);
            when(mockLocalDateTime.getSecond()).thenReturn(0);
            mocked.when(LocalDateTime::now).thenReturn(mockLocalDateTime);
            var result = LocalDateTime.now();
            assertThat(result.getSecond()).isEqualTo(0);

            when(mockItemRepository.findById(1L))
                    .thenReturn(Optional.ofNullable(item1));

            when(mockUserService.findUserById(1L))
                    .thenReturn(userDto);

            when(mockBookingRepository.findBookingByItemIdAndBookerId(1L, 1L))
                    .thenReturn(List.of(booking1, booking2));

            Comment comment = new Comment(
                    null,
                    commentFromFrontDto.getText(),
                    1L,
                    UserMapper.toUser(userDto),
                    LocalDateTime.now());

            Comment commentSaved = new Comment(
                    1L,
                    commentFromFrontDto.getText(),
                    1L,
                    UserMapper.toUser(userDto),
                    LocalDateTime.now());

            when(mockCommentRepository.save(comment))
                    .thenReturn(commentSaved);

            CommentDto commentDto = itemService.addComment(
                    1L, 1L, commentFromFrontDto);

            Assertions.assertEquals(1, commentDto.getId());
            Assertions.assertEquals(commentFromFrontDto.getText(), commentDto.getText());
            Assertions.assertEquals(1L, commentDto.getItemId());
            Assertions.assertEquals(1L, commentDto.getAuthorId());
            Assertions.assertEquals(userDto.getName(), commentDto.getAuthorName());
            Assertions.assertNotNull(commentDto.getCreated());

            when(mockBookingRepository.findBookingByItemIdAndBookerId(1L, 1L))
                    .thenReturn(new ArrayList<>());

            final BadRequestException exception = Assertions.assertThrows(
                    BadRequestException.class,
                    () -> itemService.addComment(1L, 1L, commentFromFrontDto));

            Assertions.assertEquals("пользователь не делал бронирование", exception.getMessage());

            when(mockBookingRepository.findBookingByItemIdAndBookerId(1L, 1L))
                    .thenReturn(List.of(booking4));

            final BadRequestException exception1 = Assertions.assertThrows(
                    BadRequestException.class,
                    () -> itemService.addComment(1L, 1L, commentFromFrontDto));

            Assertions.assertEquals("хотя бы одно бронирование было отклонено", exception1.getMessage());
        }
    }
}