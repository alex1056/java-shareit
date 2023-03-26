package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    User create(User user);

    User update(User user, Long userId);

    User delete(Long id);
}