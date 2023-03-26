package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(HEADER) Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        return itemService.findByIdAnyUser(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto add(@Validated @RequestBody ItemDto itemDto,
                       @RequestHeader(HEADER) Long userId) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }
}