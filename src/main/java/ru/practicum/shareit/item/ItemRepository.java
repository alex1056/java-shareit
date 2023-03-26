package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository {
    List<Item> findAll(Long userId);

    Item create(Long userId, Item item);

    Item findById(Long userId, Long id);

    Item findByIdDtoAnyUser(Long id);

    Item update(Long userId, Long itemId, Item item);

    List<Item> search(String text);
}