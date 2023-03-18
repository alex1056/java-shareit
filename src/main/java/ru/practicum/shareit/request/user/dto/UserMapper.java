package ru.practicum.shareit.request.user.dto;

import ru.practicum.shareit.request.user.model.User;


public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
        return user;
    }

    public static User toUserPutId(UserDto userDto, Long id) {
        User user = User.builder()
                .id(id)
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
        return user;
    }
}
