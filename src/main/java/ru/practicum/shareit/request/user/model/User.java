package ru.practicum.shareit.request.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class User {
    private final Long id;
    private final String email;
    private final String name;
}