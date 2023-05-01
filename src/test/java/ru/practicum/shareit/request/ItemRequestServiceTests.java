package ru.practicum.shareit.request;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;
import ru.practicum.shareit.request.dto.ItemRequestToFrontDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTests {

    @Mock
    ItemRequestRepository mockItemRequestRepository;

    @Mock
    UserService mockUserService;

    ItemRequest itemRequest0 = new ItemRequest(
            null,
            "Описание для ItemRequest 1",
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08.07482"),
            null
    );

    Item item0 = new Item(
            1L,
            "Вещь 0",
            "Описание вещи 0",
            true,
            1L,
            1L,
            null,
            null,
            null
    );

    ItemRequest itemRequest1 = new ItemRequest(
            1L,
            "Описание для ItemRequest 1",
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08.07482"),
            Set.of(item0)
    );

    ItemRequest itemRequest2 = new ItemRequest(
            2L,
            "Описание для ItemRequest 2",
            1L,
            LocalDateTime.parse("2023-04-22T19:12:08.07482"),
            Set.of(item0)
    );

    List<ItemRequest> itemRequestList = List.of(
            itemRequest1,
            itemRequest2
    );

    ItemRequestFromFrontDto itemRequestFromFrontDto = new ItemRequestFromFrontDto(
            "Описание для ItemRequest 1"
    );

    @Test
    void testFindItemRequestsFromIndex() {
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                mockItemRequestRepository, mockUserService);

        Pageable page = PageRequest.of(0, 1);

        Mockito
                .when(mockItemRequestRepository.findItemRequestPages(1L, page))
                .thenReturn(itemRequestList);

        List<ItemRequestToFrontDto> itemRequestListDto = itemRequestService.findItemRequestsFromIndex(1L, 0, 1);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(2, itemRequestListDto.size());
        Assertions.assertEquals(1, itemRequestListDto.get(0).getId());
        Assertions.assertEquals("Описание для ItemRequest 1", itemRequestListDto.get(0).getDescription());
        Assertions.assertEquals(LocalDateTime.parse("2023-04-22T19:12:08.07482"), itemRequestListDto.get(0).getCreated());
        Assertions.assertEquals(2, itemRequestListDto.get(1).getId());
        Assertions.assertEquals("Описание для ItemRequest 2", itemRequestListDto.get(1).getDescription());
        Assertions.assertEquals(LocalDateTime.parse("2023-04-22T19:12:08.07482"), itemRequestListDto.get(1).getCreated());
        Assertions.assertEquals(1, itemRequestListDto.get(1).getItems().size());
    }

    @Test
    void testGetOwnersItemRequests() {
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                mockItemRequestRepository, mockUserService);

        Mockito
                .when(mockItemRequestRepository.findItemRequestByRequesterIdOrderByCreatedDesc(1L))
                .thenReturn(itemRequestList);

        List<ItemRequestToFrontDto> itemRequestListDto = itemRequestService.getOwnersItemRequests(1L);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(2, itemRequestListDto.size());
        Assertions.assertEquals(1, itemRequestListDto.get(0).getId());
        Assertions.assertEquals("Описание для ItemRequest 1", itemRequestListDto.get(0).getDescription());
        Assertions.assertEquals(LocalDateTime.parse("2023-04-22T19:12:08.07482"), itemRequestListDto.get(0).getCreated());
        Assertions.assertEquals(2, itemRequestListDto.get(1).getId());
        Assertions.assertEquals("Описание для ItemRequest 2", itemRequestListDto.get(1).getDescription());
        Assertions.assertEquals(LocalDateTime.parse("2023-04-22T19:12:08.07482"), itemRequestListDto.get(1).getCreated());
    }

    @Test
    void testCreateItemRequest() {
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                mockItemRequestRepository, mockUserService);

        Mockito
                .when(mockItemRequestRepository.save(Mockito.<ItemRequest>any()))
                .thenReturn(itemRequest1);

        ItemRequestToFrontDto itemRequestToFrontDto = itemRequestService.createItemRequest(1L, itemRequestFromFrontDto);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, itemRequestToFrontDto.getId());
        Assertions.assertEquals("Описание для ItemRequest 1", itemRequestToFrontDto.getDescription());
        Assertions.assertEquals(LocalDateTime.parse("2023-04-22T19:12:08.07482"), itemRequestToFrontDto.getCreated());
    }

    @Test
    void testFindItemRequestById() {
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                mockItemRequestRepository, mockUserService);

        Mockito
                .when(mockItemRequestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(itemRequest1));

        ItemRequestToFrontDto itemRequestToFrontDto = itemRequestService.findItemRequestById(1L, 1L);
        System.out.println(itemRequestToFrontDto);

        Mockito.verify(mockUserService, Mockito.times(1))
                .findUserById(1L);

        Assertions.assertEquals(1, itemRequestToFrontDto.getId());
        Assertions.assertEquals("Описание для ItemRequest 1", itemRequestToFrontDto.getDescription());
        Assertions.assertEquals(LocalDateTime.parse("2023-04-22T19:12:08.07482"), itemRequestToFrontDto.getCreated());
        Assertions.assertEquals(1, itemRequestToFrontDto.getItems().get(0).getId());

        Mockito
                .when(mockItemRequestRepository.findById(2L))
                .thenThrow(new NotFoundException("itemRequast c id=2"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findItemRequestById(1L, 2L));

        Assertions.assertEquals("itemRequast c id=2", exception.getMessage());
    }
}