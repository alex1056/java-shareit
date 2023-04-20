package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestFromFrontDto {
    @NotNull(message = "У запроса должно быть описание")
    @NotEmpty(message = "Описание должно быть заполнено")
    private String description;
}
