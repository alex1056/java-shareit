package ru.practicum.shareit.request.user;

import ru.practicum.shareit.request.user.dto.UserDto;
import ru.practicum.shareit.request.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    User delete(Long id);
}