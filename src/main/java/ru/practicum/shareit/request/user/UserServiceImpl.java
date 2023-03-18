package ru.practicum.shareit.request.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.user.dto.UserDto;
import ru.practicum.shareit.request.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User createUser(UserDto userDto) {
        return repository.create(userDto);
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) {
        return repository.update(userDto, userId);
    }

    @Override
    public User deleteUser(Long id) {
        return repository.delete(id);
    }
}