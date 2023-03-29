package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getAll(Long userId) {
        userRepository.findById(userId);
        List<Item> itemList = repository.findAll(userId);
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> itemList = repository.search(text);
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        userRepository.findById(userId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(repository.create(userId, item));
    }

    @Override
    public ItemDto findById(long userId, Long id) {
        userRepository.findById(userId);
        return ItemMapper.toItemDto(repository.findById(userId, id));
    }

    @Override
    public ItemDto findByIdAnyUser(Long id) {
        return ItemMapper.toItemDto(repository.findByIdDtoAnyUser(id));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userRepository.findById(userId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(repository.update(userId, itemId, item));
    }
}