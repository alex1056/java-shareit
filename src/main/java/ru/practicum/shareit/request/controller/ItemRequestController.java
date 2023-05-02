package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;
import ru.practicum.shareit.request.dto.ItemRequestToFrontDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestToFrontDto add(@Validated @RequestBody ItemRequestFromFrontDto itemRequestFromFrontDto,
                                     @RequestHeader(HEADER) Long userId) {
        return itemRequestService.createItemRequest(userId, itemRequestFromFrontDto);
    }

    @GetMapping
    public List<ItemRequestToFrontDto> findByRequesterId(@RequestHeader(HEADER) Long userId) {
        return itemRequestService.getOwnersItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestToFrontDto> findAllItemRequests(
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "100") @Min(1) Integer size,
            @RequestHeader(HEADER) Long userId) {
        return itemRequestService.findItemRequestsFromIndex(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestToFrontDto findItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader(HEADER) Long userId
    ) {
        return itemRequestService.findItemRequestById(userId, requestId);
    }
}