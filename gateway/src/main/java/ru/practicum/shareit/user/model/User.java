package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;

    public String getName() {
        return name != null ? name.trim() : name;
    }

    public String getEmail() {
        if (email != null) return email.trim();
        return email;
    }
}