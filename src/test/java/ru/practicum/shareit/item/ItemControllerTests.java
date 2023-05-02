package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentFromFrontDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = ItemController.class)
class ItemControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    ItemDto itemDto0 = new ItemDto(
            null,
            null,
            "Описание вещи 0",
            true,
            null,
            null,
            null
    );

    ItemDto itemDto1 = new ItemDto(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null
    );

    ItemCommentDto itemCommentDto1 = new ItemCommentDto(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null,
            null
    );

    CommentFromFrontDto commentFromFrontDto = new CommentFromFrontDto(
            1L,
            "Комментарий 1"
    );

    CommentDto commentDto0 = new CommentDto(
            1L,
            "Комментарий 1",
            1L,
            1L,
            "Пользователь 1",
            LocalDateTime.parse("2023-04-22T19:12:08")
    );

    ItemCommentDto itemCommentDto2 = new ItemCommentDto(
            1L,
            "Вещь 1",
            "Описание вещи 1",
            true,
            1L,
            null,
            null,
            Set.of(commentDto0)
    );

    @Test
    void testAdd() throws Exception {

        when(itemService.createItem(1L, itemDto1))
                .thenReturn(itemDto1);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.name").value(itemDto1.getName()))
                .andExpect(jsonPath("$.description").value(itemDto1.getDescription()));

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto0))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAll() throws Exception {

        when(itemService.getAll(any(), any(), any()))
                .thenReturn(List.of(itemCommentDto1, itemCommentDto1));

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemCommentDto1.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemCommentDto1.getName().toString()))
                .andExpect(jsonPath("$.[0].description").value(itemCommentDto1.getDescription()));

        Assertions.assertThatThrownBy(() ->
                        mvc.perform(get("/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .param("from", String.valueOf(0))
                                .param("size", String.valueOf(0))
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON))
                )
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("getAll.size");
    }

    @Test
    void testAddComment() throws Exception {

        when(itemService.addComment(1L, 1L, commentFromFrontDto))
                .thenReturn(commentDto0);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(commentFromFrontDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto0.getId()))
                .andExpect(jsonPath("$.text").value(commentDto0.getText()))
                .andExpect(jsonPath("$.itemId").value(commentDto0.getItemId()))
                .andExpect(jsonPath("$.authorName").value(commentDto0.getAuthorName()))
                .andExpect(jsonPath("$.created").value(commentDto0.getCreated().toString()));
    }

    @Test
    void testFindById() throws Exception {

        when(itemService.findByIdAnyUser(any(), any()))
                .thenReturn(itemCommentDto2);

        mvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemCommentDto1.getId()))
                .andExpect(jsonPath("$.name").value(itemCommentDto1.getName().toString()))
                .andExpect(jsonPath("$.description").value(itemCommentDto1.getDescription()))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments.[0].id").value(commentDto0.getId()))
                .andExpect(jsonPath("$.comments.[0].text").value(commentDto0.getText()))
                .andExpect(jsonPath("$.comments.[0].itemId").value(commentDto0.getItemId()))
                .andExpect(jsonPath("$.comments.[0].authorId").value(commentDto0.getAuthorId()))
                .andExpect(jsonPath("$.comments.[0].authorName").value(commentDto0.getAuthorName()))
                .andExpect(jsonPath("$.comments.[0].created").value(commentDto0.getCreated().toString()));
    }

    @Test
    void testSearchItems() throws Exception {

        when(itemService.search(any(), any(), any()))
                .thenReturn(List.of(itemDto1, itemDto1));

        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .param("text", "abc")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemDto1.getName().toString()))
                .andExpect(jsonPath("$.[0].description").value(itemDto1.getDescription()))
                .andExpect(jsonPath("$.[0].available").value(itemDto1.isAvailable()))
                .andExpect(jsonPath("$.[0].requestId").value(itemDto1.getRequestId()));

        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .param("text", "")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").doesNotExist());
    }

    @Test
    void testUpdate() throws Exception {

        when(itemService.updateItem(1L, 1L, itemDto1))
                .thenReturn(itemDto1);

        mvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.name").value(itemDto1.getName()))
                .andExpect(jsonPath("$.description").value(itemDto1.getDescription()));
    }
}