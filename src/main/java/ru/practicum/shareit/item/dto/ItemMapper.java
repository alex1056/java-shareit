package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
//                item.getRequest() != null ? item.getRequest().getId() : null
                .request(null)
                .build();
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, Long id, Long owner,
                              ItemRequest request
    ) {
        Item itemToSave = Item.builder()
                .id(id)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .owner(owner)
                .request(request)
                .build();
        return itemToSave;
    }
}
