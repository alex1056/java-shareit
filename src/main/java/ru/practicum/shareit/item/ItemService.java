package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

interface ItemService {
    List<ItemDto> getAll(Long userId);

    List<ItemDto> search(String text);

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto findById(long userId, Long id);

    ItemDto findByIdAnyUser(Long id);
}