package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentNewDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
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
    public List<ItemCommentsDto> getAll(@RequestHeader(HEADER) Long userId) {
        return itemService.getAll(userId);
    }

    @PostMapping
    public ItemDto add(@Validated @RequestBody ItemDto itemDto,
                       @RequestHeader(HEADER) Long userId) {
        return itemService.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @Validated @RequestBody CommentNewDto commentNewDto,
                                 @RequestHeader(HEADER) Long userId) {
        return itemService.addComment(userId, itemId, commentNewDto);
    }

    @GetMapping("/{itemId}")
    public ItemCommentsDto findById(@PathVariable Long itemId,
                                    @RequestHeader(HEADER) Long userId) {
        return itemService.findByIdAnyUser(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemService.search(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }
}