package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private final Long id;
    @NotNull(message = "У вещи должно быть название")
    @NotEmpty(message = "Название должно быть заполнено")
    private final String name;
    @NotNull(message = "У вещи должно быть описание")
    private final String description;
    @NotNull(message = "Укажите доступность для аренды вещи")
    private final Boolean available;
    private final Long request;

    public Boolean isAvailable() {
        return available;
    }
}