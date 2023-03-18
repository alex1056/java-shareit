package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Item {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final Long owner;
    private final ItemRequest request;

    public Boolean isAvailable() {
        return available;
    }

}
