package ru.practicum.shareit.request.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private final Long id;
    @NotNull(message = "У пользователя должна быть электронная почта")
    @Email(message = "Неправильный формат электронной почты")
    private final String email;
    private final String name;
}

