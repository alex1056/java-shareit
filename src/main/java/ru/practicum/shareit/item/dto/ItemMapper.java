package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(item.getId(),
                item.getName().trim(),
                item.getDescription().trim(),
                item.isAvailable(),
                item.getRequestId(),
                item.getLastBooking(),
                item.getNextBooking()
        );
        return itemDto;
    }

    public static List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemDto(item));
        }
        return result;
    }

    public static ItemCommentDto toItemDtoComments(Item item) {
        ItemCommentDto itemCommentDto = new ItemCommentDto(item.getId(),
                item.getName().trim(),
                item.getDescription().trim(),
                item.isAvailable(),
                null,
                item.getLastBooking(),
                item.getNextBooking(),
                CommentMapper.toCommentDto(item.getComments())
        );
        return itemCommentDto;
    }

    public static List<ItemCommentDto> toItemDtoComments(Iterable<Item> items) {
        List<ItemCommentDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemDtoComments(item));
        }
        return result;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                null,
                itemDto.getRequestId(),
                null,
                null,
                null
        );
        return item;
    }

    public static ItemRequestDto toItemRequestDto(Item item) {
        ItemRequestDto itemRequestDto = new ItemRequestDto(item.getId(),
                item.getName().trim(),
                item.getDescription().trim(),
                item.isAvailable(),
                item.getRequestId()
        );
        return itemRequestDto;
    }

    public static List<ItemRequestDto> toItemRequestDto(Iterable<Item> items) {
        List<ItemRequestDto> result = new ArrayList<>();
        if (items == null) return result;
        for (Item item : items) {
            result.add(toItemRequestDto(item));
        }
        return result;
    }
}
