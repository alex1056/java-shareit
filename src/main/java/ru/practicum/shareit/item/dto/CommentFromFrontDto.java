package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFromFrontDto {
    private Long id;
    @NotNull(message = "Должен быть не пустой комментарий")
    @NotEmpty(message = "Комментарий должен быть заполнен")
    private String text;
}
