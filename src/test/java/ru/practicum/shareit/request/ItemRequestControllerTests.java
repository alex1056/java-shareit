package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestFromFrontDto;
import ru.practicum.shareit.request.dto.ItemRequestToFrontDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = ItemRequestController.class)
class ItemRequestControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    ItemRequestFromFrontDto itemRequestFromFrontDto = new ItemRequestFromFrontDto(
            "Описание для ItemRequest 1"
    );

    ItemRequestFromFrontDto itemRequestFromFrontDtoEmpty = new ItemRequestFromFrontDto();

    ItemRequestToFrontDto itemRequestToFrontDto1 = new ItemRequestToFrontDto(
            1L,
            "Описание для ItemRequest 1",
            LocalDateTime.parse("2023-04-22T19:12:08"),
            null
    );

    ItemRequestToFrontDto itemRequestToFrontDto2 = new ItemRequestToFrontDto(
            2L,
            "Описание для ItemRequest 2",
            LocalDateTime.parse("2023-04-22T19:12:09"),
            null
    );

    List<ItemRequestToFrontDto> itemRequestToFrontDtoList = List.of(itemRequestToFrontDto1, itemRequestToFrontDto2);

    @Test
    void testAdd() throws Exception {

        when(itemRequestService.createItemRequest(any(), any()))
                .thenReturn(itemRequestToFrontDto1);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestToFrontDto1.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestToFrontDto1.getDescription()))
                .andExpect(jsonPath("$.created").value(itemRequestToFrontDto1.getCreated().toString()));

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestFromFrontDtoEmpty))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindByRequesterId() throws Exception {

        when(itemRequestService.getOwnersItemRequests(any()))
                .thenReturn(itemRequestToFrontDtoList);

        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[1]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemRequestToFrontDto1.getId()))
                .andExpect(jsonPath("$.[0].description").value(itemRequestToFrontDto1.getDescription()))
                .andExpect(jsonPath("$.[0].created").value(itemRequestToFrontDto1.getCreated().toString()))
                .andExpect(jsonPath("$.[1].id").value(itemRequestToFrontDto2.getId()))
                .andExpect(jsonPath("$.[1].description").value(itemRequestToFrontDto2.getDescription()))
                .andExpect(jsonPath("$.[1].created").value(itemRequestToFrontDto2.getCreated().toString()));
    }

    @Test
    void testFindAllItemRequests() throws Exception {

        when(itemRequestService.findItemRequestsFromIndex(any(), any(), any()))
                .thenReturn(itemRequestToFrontDtoList);

        mvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemRequestToFrontDto1.getId()))
                .andExpect(jsonPath("$.[0].description").value(itemRequestToFrontDto1.getDescription()))
                .andExpect(jsonPath("$.[0].created").value(itemRequestToFrontDto1.getCreated().toString()));
    }

    @Test
    void testFindAllItemRequestsRequestParams() throws Exception {

        when(itemRequestService.findItemRequestsFromIndex(any(), any(), any()))
                .thenReturn(itemRequestToFrontDtoList);

        Assertions
                .assertThatThrownBy(
                        () -> mvc.perform(get("/requests/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                                .param("from", String.valueOf(0))
                                .param("size", String.valueOf(-1))
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON))
                )
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("findAllItemRequests.size");

        Assertions
                .assertThatThrownBy(
                        () -> mvc.perform(get("/requests/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                                .param("from", String.valueOf(1))
                                .param("size", String.valueOf(-1))
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON))
                )
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("findAllItemRequests.size");
    }

    @Test
    void testFindItemRequestById() throws Exception {

        when(itemRequestService.findItemRequestById(any(), any()))
                .thenReturn(itemRequestToFrontDto1);

        mvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestFromFrontDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestToFrontDto1.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestToFrontDto1.getDescription()))
                .andExpect(jsonPath("$.created").value(itemRequestToFrontDto1.getCreated().toString()));
    }
}