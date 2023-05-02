package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.helper.Helpers;
import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestToFrontDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;

    @Override
    public ItemRequestToFrontDto createItemRequest(Long userId, ItemRequestFromFrontDto itemRequestDto) {
        userService.findUserById(userId);
        ItemRequest itemRequestResult = new ItemRequest(
                null,
                itemRequestDto.getDescription(),
                userId,
                LocalDateTime.now(),
                null
        );
        return ItemRequestMapper.toItemRequestToFrontDto(repository.save(itemRequestResult));
    }

    @Override
    public List<ItemRequestToFrontDto> getOwnersItemRequests(Long userId) {
        userService.findUserById(userId);
        return ItemRequestMapper.toItemRequestToFrontDto(repository.findItemRequestByRequesterIdOrderByCreatedDesc(userId));
    }

    @Override
    public List<ItemRequestToFrontDto> findItemRequestsFromIndex(Long ownerId, Integer from, Integer size) {
        userService.findUserById(ownerId);
        Pageable page = PageRequest.of(Helpers.getPageNumber(from, size), size);
        return ItemRequestMapper.toItemRequestToFrontDto(repository.findItemRequestPages(ownerId, page));
    }

    @Override
    public ItemRequestToFrontDto findItemRequestById(Long userId, Long requestId) {
        userService.findUserById(userId);
        Optional<ItemRequest> itemRequest = repository.findById(requestId);
        itemRequest.orElseThrow(() -> new NotFoundException("itemRequast c id=" + requestId));
        return ItemRequestMapper.toItemRequestToFrontDto(itemRequest.get());
    }
}