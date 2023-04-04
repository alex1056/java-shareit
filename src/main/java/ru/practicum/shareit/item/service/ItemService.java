package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentNewDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemCommentsDto> getAll(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto findById(Long userId, Long id);

    ItemCommentsDto findByIdAnyUser(Long userId, Long id);

    List<ItemDto> search(String text);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    CommentDto addComment(Long userId, Long itemId, CommentNewDto commentNewDto);

}