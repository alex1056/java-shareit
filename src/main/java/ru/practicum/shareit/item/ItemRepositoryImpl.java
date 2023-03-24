package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final Map<Long, Item> items = new HashMap<>();
    private static Long id = 0L;

    @Override
    public List<ItemDto> findAll(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto, ItemRequest request) {
        Item item = ItemMapper.toItem(itemDto, ++id, userId, request);
        ItemDto itemResult = ItemMapper.toItemDto(item);
        items.put(id, item);
        return itemResult;
    }

    private Item findById(Long userId, Long id) {
        Optional<Item> itemResult = items.values().stream().filter(item -> item.getId().equals(id) && item.getOwner().equals(userId)).findAny();
        if (itemResult.isEmpty()) {
            throw new NotFoundException("item c id= " + id + " и userId= " + userId);
        }
        return itemResult.get();
    }

    @Override
    public ItemDto findByIdDto(Long userId, Long id) {
        Item itemResult = findById(userId, id);
        return ItemMapper.toItemDto(itemResult);
    }

    @Override
    public ItemDto findByIdDtoAnyUser(Long id) {
        Optional<Item> itemResult = items.values().stream().filter(item -> item.getId().equals(id)).findAny();
        if (itemResult.isEmpty()) {
            throw new NotFoundException("item c id= " + id);
        }
        return ItemMapper.toItemDto(itemResult.get());
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = findById(userId, itemId);
        items.remove(itemId);
        Item itemToSave = composeFieldsForUpdate(item, itemDto);
        items.put(itemId, itemToSave);
        return ItemMapper.toItemDto(itemToSave);
    }

    private Item composeFieldsForUpdate(Item item, ItemDto itemDto) {
        Item itemResult = Item.builder()
                .id(item.getId())
                .name(itemDto.getName() != null ? itemDto.getName() : item.getName())
                .description(itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription())
                .available(itemDto.isAvailable() != null ? itemDto.isAvailable() : item.isAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
        return itemResult;
    }


    @Override
    public List<ItemDto> search(String text) {
        String textLowerCase = text.toLowerCase();
        List<Item> itemList = items.values().stream().filter(item -> item.isAvailable() && (item.getName().toLowerCase().contains(textLowerCase)
                        || item.getDescription().toLowerCase().contains(textLowerCase)))
                .collect(Collectors.toList());
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
