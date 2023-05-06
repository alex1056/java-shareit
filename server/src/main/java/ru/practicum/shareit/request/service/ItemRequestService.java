package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;
import ru.practicum.shareit.request.dto.ItemRequestToFrontDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestToFrontDto createItemRequest(Long userId, ItemRequestFromFrontDto itemRequetsDto);

    List<ItemRequestToFrontDto> getOwnersItemRequests(Long userId);

    List<ItemRequestToFrontDto> findItemRequestsFromIndex(Long ownerId, Integer from, Integer size);

    ItemRequestToFrontDto findItemRequestById(Long userId, Long requestId);
}