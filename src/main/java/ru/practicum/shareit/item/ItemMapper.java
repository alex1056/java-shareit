package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

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

    public static Item toItem(ItemDto itemDto) {
        Item itemToSave = Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .owner(null)
                .request(itemDto.getRequest())
                .build();
        return itemToSave;
    }
}
