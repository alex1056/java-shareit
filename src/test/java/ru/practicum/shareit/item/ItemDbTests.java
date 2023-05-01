package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.helper.Helpers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemDbTests {
    @Autowired
    private EntityManager em;
    @Autowired
    private ItemRequestService service;

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    UserDto userDto1 = new UserDto(null, "user1@mail.ru", "Пётр");

    ItemDto itemDto0 = new ItemDto(
            null,
            "Отвертка со сменными насадками 1",
            "Хорошая отвертка",
            true,
            1L,
            null,
            null
    );

    ItemDto itemDto1 = new ItemDto(
            null,
            "Отвертка со сменными насадками 2",
            "Хорошая отвертка",
            true,
            1L,
            null,
            null
    );

    @Test
    void testCreateItem() {
        userService.saveUser(userDto1);
        itemService.createItem(1L, itemDto0);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = 1", Item.class);
        Item item = query.getSingleResult();
        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo(itemDto0.getName()));
        assertThat(item.getDescription(), equalTo(itemDto0.getDescription()));
        assertThat(item.isAvailable(), equalTo(itemDto0.isAvailable()));
    }

    @Test
    void testFindItemByOwnerId() {
        userService.saveUser(userDto1);
        itemService.createItem(1L, itemDto0);
        Pageable page = PageRequest.of(Helpers.getPageNumber(0, 2), 2);
        List<Item> item = itemRepository.findItemByOwnerId(1L, page);
        assertThat(item.size(), equalTo(1));
        assertThat(item.get(0).getId(), equalTo(1L));
        assertThat(item.get(0).getName(), equalTo(itemDto0.getName()));
        assertThat(item.get(0).getDescription(), equalTo(itemDto0.getDescription()));
        assertThat(item.get(0).isAvailable(), equalTo(itemDto0.isAvailable()));
        assertThat(item.get(0).getRequestId(), equalTo(itemDto0.getRequestId()));
        assertThat(item.get(0).getLastBooking(), nullValue());
    }

    @Test
    void testFindItemByOwnerIdAndId() {
        userService.saveUser(userDto1);
        itemService.createItem(1L, itemDto0);
        Optional<Item> item = itemRepository.findItemByOwnerIdAndId(1L, 1L);
        assertThat(item.isPresent(), equalTo(true));
        assertThat(item.get().getId(), equalTo(1L));
        assertThat(item.get().getName(), equalTo(itemDto0.getName()));
        assertThat(item.get().getDescription(), equalTo(itemDto0.getDescription()));
        assertThat(item.get().isAvailable(), equalTo(itemDto0.isAvailable()));
        assertThat(item.get().getRequestId(), equalTo(itemDto0.getRequestId()));
        assertThat(item.get().getLastBooking(), nullValue());

        item = itemRepository.findItemByOwnerIdAndId(2L, 1L);
        assertThat(item.isEmpty(), equalTo(true));
    }

    @Test
    void testSearch() {
        userService.saveUser(userDto1);
        itemService.createItem(1L, itemDto0);
        itemService.createItem(1L, itemDto1);
        Pageable page = PageRequest.of(Helpers.getPageNumber(0, 2), 2);
        List<Item> itemList = itemRepository.search("сменными", page);
        assertThat(itemList.size(), equalTo(2));
        itemList = itemRepository.search("abc", page);
        assertThat(itemList.size(), equalTo(0));
    }
}