package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = repository.findAll();
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        return UserMapper.toUserDto(repository.findById(id));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.create(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.update(user, userId));
    }

    @Override
    public UserDto deleteUser(Long id) {
        return UserMapper.toUserDto(repository.delete(id));
    }
}