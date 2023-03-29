package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public List<Item> findAll(Long userId) {
        return items.values().stream()
                .filter(item -> userId.equals(item.getOwner()))
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Long userId, Item item) {
        item.setOwner(userId);
        item.setId(++id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item findById(Long userId, Long id) {
        Optional<Item> itemResult = items.values().stream().filter(item -> item.getId().equals(id) && item.getOwner().equals(userId)).findAny();
        if (itemResult.isEmpty()) {
            throw new NotFoundException("item c id= " + id + " и userId= " + userId);
        }
        return itemResult.get();
    }

    @Override
    public Item findByIdDtoAnyUser(Long id) {
        Optional<Item> itemResult = items.values().stream().filter(item -> item.getId().equals(id)).findAny();
        if (itemResult.isEmpty()) {
            throw new NotFoundException("item c id= " + id);
        }
        return itemResult.get();
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        Item itemFound = findById(userId, itemId);
        items.remove(itemId);
        Item itemToSave = composeFieldsForUpdate(itemFound, item);
        items.put(itemId, itemToSave);
        return itemToSave;
    }

    private Item composeFieldsForUpdate(Item item, Item itemFromDto) {
        return Item.builder()
                .id(item.getId())
                .name(itemFromDto.getName() != null ? itemFromDto.getName() : item.getName())
                .description(itemFromDto.getDescription() != null ? itemFromDto.getDescription() : item.getDescription())
                .available(itemFromDto.isAvailable() != null ? itemFromDto.isAvailable() : item.isAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }


    @Override
    public List<Item> search(String text) {
        String textLowerCase = text.toLowerCase();
        return items.values().stream().filter(item -> item.isAvailable() && (item.getName().toLowerCase().contains(textLowerCase)
                        || item.getDescription().toLowerCase().contains(textLowerCase)))
                .collect(Collectors.toList());
    }
}
