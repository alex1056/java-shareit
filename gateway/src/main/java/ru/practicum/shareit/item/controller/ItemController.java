package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentFromFrontDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;
import java.util.ArrayList;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "100") @Min(1) Integer size,
            @RequestHeader(HEADER) Long userId
    ) {
        return itemClient.getAllItems(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Validated @RequestBody ItemDto itemDto,
                                      @RequestHeader(HEADER) Long userId) {
        return itemClient.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                             @Validated @RequestBody CommentFromFrontDto commentFromFrontDto,
                                             @RequestHeader(HEADER) Long userId) {
        return itemClient.addComment(userId, itemId, commentFromFrontDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Long itemId,
                                           @RequestHeader(HEADER) Long userId) {
        return itemClient.findByIdAnyUser(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "100") @Min(1) Integer size,
            @RequestParam String text
    ) {
        if (text.isEmpty()) return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return itemClient.search(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }
}