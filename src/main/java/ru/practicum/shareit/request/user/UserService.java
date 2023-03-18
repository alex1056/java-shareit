package ru.practicum.shareit.request.user;

import ru.practicum.shareit.request.user.dto.UserDto;
import ru.practicum.shareit.request.user.model.User;

import java.util.List;

interface UserService {
    List<User> getAllUsers();

    User findById(Long id);

    User createUser(UserDto userDto);

    User updateUser(UserDto userDto, Long userId);

    User deleteUser(Long id);
}