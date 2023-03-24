package ru.practicum.shareit.request.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private final Long id;
    private final String email;
    private final String name;
}