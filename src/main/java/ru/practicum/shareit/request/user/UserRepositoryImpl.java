package ru.practicum.shareit.request.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.SmthAlreadyExistException;
import ru.practicum.shareit.request.user.dto.UserDto;
import ru.practicum.shareit.request.user.model.User;
import ru.practicum.shareit.request.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("пользователь с id: " + id);
        }
        return user;
    }

    @Override
    public User create(UserDto userDto) {
        if (isUserExist(userDto.getEmail())) {
            throw new SmthAlreadyExistException("пользователь с email: " + userDto.getEmail());
        }
        User user = UserMapper.toUserPutId(userDto, ++id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(UserDto userDto, Long userId) {
        User user = findById(userId);

        if (isUserExist(userDto.getEmail()) && !userDto.getEmail().equals(user.getEmail())) {
            throw new SmthAlreadyExistException("пользователь с email: " + userDto.getEmail() + " email занят, невозможно изменить");
        }

        users.remove(userId);
        User updatedUser = composeFieldsForUpdate(user, userDto);
        users.put(userId, updatedUser);
        return updatedUser;
    }

    private User composeFieldsForUpdate(User user, UserDto userDto) {
        User userResult = User.builder()
                .id(user.getId())
                .email(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail())
                .name(userDto.getName() != null ? userDto.getName() : user.getName())
                .build();
        return userResult;
    }

    @Override
    public User delete(Long id) {
        User user = findById(id);
        users.remove(id);
        return user;
    }

    private boolean isUserExist(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).count() > 0;
    }
}
