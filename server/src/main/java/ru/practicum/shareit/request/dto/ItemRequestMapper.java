package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestToFrontDto toItemRequestToFrontDto(ItemRequest request) {
        ItemRequestToFrontDto requestDto = new ItemRequestToFrontDto(request.getId(),
                request.getDescription().trim(),
                request.getCreated(),
                ItemMapper.toItemRequestDto(request.getItems())
        );
        return requestDto;
    }

    public static List<ItemRequestToFrontDto> toItemRequestToFrontDto(Iterable<ItemRequest> requests) {
        List<ItemRequestToFrontDto> result = new ArrayList<>();
        for (ItemRequest request : requests) {
            result.add(toItemRequestToFrontDto(request));
        }
        return result;
    }
}
