package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentFromFrontDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemCommentDto> getAll(Long userId, Integer from, Integer size);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto findById(Long userId, Long id);

    ItemCommentDto findByIdAnyUser(Long userId, Long id);

    List<ItemDto> search(String text, Integer from, Integer size);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    CommentDto addComment(Long userId, Long itemId, CommentFromFrontDto commentFromFrontDto);

}