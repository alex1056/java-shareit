package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> add(
            @Validated @RequestBody ItemRequestFromFrontDto itemRequestFromFrontDto,
            @RequestHeader(HEADER) Long userId) {
        return itemRequestClient.createItemRequest(userId, itemRequestFromFrontDto);
    }

    @GetMapping
    public ResponseEntity<Object> findByRequesterId(@RequestHeader(HEADER) Long userId) {
        return itemRequestClient.getOwnersItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequests(
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "100") @Min(1) Integer size,
            @RequestHeader(HEADER) Long userId) {
        return itemRequestClient.findItemRequestsFromIndex(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@PathVariable Long requestId,
                                                      @RequestHeader(HEADER) Long userId
    ) {
        return itemRequestClient.findItemRequestById(userId, requestId);
    }
}