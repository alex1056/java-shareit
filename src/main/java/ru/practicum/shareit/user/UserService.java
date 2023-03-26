package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();

    UserDto findById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    UserDto deleteUser(Long id);
}