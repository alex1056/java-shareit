package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentFromFrontDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemCommentDto> getAll(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestHeader(HEADER) Long userId
    ) {
        return itemService.getAll(userId, from, size);
    }

    @PostMapping
    public ItemDto add(@RequestBody ItemDto itemDto,
                       @RequestHeader(HEADER) Long userId) {
        return itemService.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestBody CommentFromFrontDto commentFromFrontDto,
                                 @RequestHeader(HEADER) Long userId) {
        return itemService.addComment(userId, itemId, commentFromFrontDto);
    }

    @GetMapping("/{itemId}")
    public ItemCommentDto findById(@PathVariable Long itemId,
                                   @RequestHeader(HEADER) Long userId) {
        return itemService.findByIdAnyUser(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestParam String text
    ) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemService.search(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }
}