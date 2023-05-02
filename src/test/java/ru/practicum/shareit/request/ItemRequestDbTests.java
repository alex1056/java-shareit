package ru.practicum.shareit.request;

import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;
import ru.practicum.shareit.request.dto.ItemRequestToFrontDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestDbTests {
    @Autowired
    private EntityManager em;
    @Autowired
    private ItemRequestService service;

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    UserDto userDto1 = new UserDto(null, "user1@mail.ru", "Пётр");
    UserDto userDto2 = new UserDto(null, "user2@mail.ru", "Василий");
    ItemRequest itemRequest0 = new ItemRequest(
            null,
            "Описание для ItemRequest 1",
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08.07482"),
            null
    );

    ItemRequestFromFrontDto itemRequestFromFrontDto1 = new ItemRequestFromFrontDto(
            "Описание для ItemRequest 1"
    );

    ItemRequestFromFrontDto itemRequestFromFrontDto2 = new ItemRequestFromFrontDto(
            "Описание для ItemRequest 2"
    );

    ItemRequestFromFrontDto itemRequestFromFrontDto3 = new ItemRequestFromFrontDto(
            "Описание для ItemRequest 3"
    );

    ItemRequestDto itemRequestDto = new ItemRequestDto(
            null,
            "Отвертка со сменными насадками",
            "Хорошая отвертка",
            true,
            1L
    );

    ItemDto itemDto = new ItemDto(
            null,
            "Отвертка со сменными насадками",
            "Хорошая отвертка",
            true,
            1L,
            null,
            null
    );

    @Test
    void testCreateItemRequest() {
        userService.saveUser(userDto1);
        service.createItemRequest(1L, itemRequestFromFrontDto1);

        TypedQuery<ItemRequest> query = em.createQuery("Select r from ItemRequest r where r.id = 1", ItemRequest.class);
        ItemRequest itemRequest = query.getSingleResult();
        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo(itemRequest0.getDescription()));
        assertThat(itemRequest.getCreated(), LocalDateTimeMatchers.before(LocalDateTime.now()));
    }

    @Test
    void testGetOwnersItemRequests() {
        userService.saveUser(userDto1);

        service.createItemRequest(1L, itemRequestFromFrontDto1);

        itemService.createItem(1L, itemDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select r from ItemRequest r where r.id = 1", ItemRequest.class);
        ItemRequest itemRequest = query.getSingleResult();
        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo(itemRequest0.getDescription()));
        assertThat(itemRequest.getCreated(), LocalDateTimeMatchers.before(LocalDateTime.now()));
        assertThat(itemRequest.getItems(), hasSize(1));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).getId(), equalTo(1L));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).getName(), equalTo(itemDto.getName()));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).getDescription(), equalTo(itemDto.getDescription()));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).isAvailable(), equalTo(itemDto.isAvailable()));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).getRequestId(), equalTo(itemDto.getRequestId()));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).getLastBooking(), equalTo(null));
        assertThat(new ArrayList<>(itemRequest.getItems()).get(0).getNextBooking(), equalTo(null));
    }

    @Test
    void testFindItemRequestsFromIndex() {
        userService.saveUser(userDto1);
        userService.saveUser(userDto2);

        service.createItemRequest(1L, itemRequestFromFrontDto1);
        service.createItemRequest(1L, itemRequestFromFrontDto2);
        service.createItemRequest(1L, itemRequestFromFrontDto3);

        List<ItemRequestToFrontDto> itemRequestToFrontDtoList = service.findItemRequestsFromIndex(2L, 0, 2);
        assertThat(itemRequestToFrontDtoList.size(), equalTo(2));
        List<ItemRequestToFrontDto> itemRequestToFrontDtoList1 = service.findItemRequestsFromIndex(2L, 1, 2);
        assertThat(itemRequestToFrontDtoList1.size(), equalTo(2));
        assertThat(itemRequestToFrontDtoList1.get(0).getId(), equalTo(3L)); // самый последний вначале
        assertThat(itemRequestToFrontDtoList1.get(0).getDescription(), equalTo(itemRequestFromFrontDto3.getDescription()));

    }

    @Test
    void testFindItemRequestById() {
        userService.saveUser(userDto1);
        userService.saveUser(userDto2);

        service.createItemRequest(1L, itemRequestFromFrontDto1);
        service.createItemRequest(1L, itemRequestFromFrontDto2);
        service.createItemRequest(1L, itemRequestFromFrontDto3);
        ItemDto itemDto1 = itemService.createItem(1L, itemDto);
        itemService.createItem(1L, itemDto1);

        ItemRequestToFrontDto itemRequestToFrontDto = service.findItemRequestById(1L, 2L);
        assertThat(itemRequestToFrontDto.getId(), equalTo(2L));
        assertThat(itemRequestToFrontDto.getDescription(), equalTo(itemRequestFromFrontDto2.getDescription()));
    }
}