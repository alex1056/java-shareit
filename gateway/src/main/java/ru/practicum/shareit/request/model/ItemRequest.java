package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemRequest {
    private Long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private Set<Item> items;

    public String getDescription() {
        return description != null ? description.trim() : description;
    }
}

