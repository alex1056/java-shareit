package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getAll(Long userId) {
        userRepository.findById(userId);
        return repository.findAll(userId);
    }

    @Override
    public List<ItemDto> search(String text) {
        return repository.search(text);
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        userRepository.findById(userId);
        return repository.create(userId, itemDto, null);
    }

    @Override
    public ItemDto findById(long userId, Long id) {
        userRepository.findById(userId);
        return repository.findByIdDto(userId, id);
    }

    @Override
    public ItemDto findByIdAnyUser(Long id) {
        return repository.findByIdDtoAnyUser(id);
    }


    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userRepository.findById(userId);
        return repository.update(userId, itemId, itemDto);
    }
}