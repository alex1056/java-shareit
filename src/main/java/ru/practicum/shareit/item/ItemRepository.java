package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

interface ItemRepository {
    List<ItemDto> findAll(Long userId);

    ItemDto create(Long userId, ItemDto itemDto, ItemRequest request);

    ItemDto findByIdDto(Long userId, Long id);

    ItemDto findByIdDtoAnyUser(Long id);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> search(String text);
}