package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EntityAlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

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
    public User create(User user) {
        if (isUserExist(user.getEmail())) {
            throw new EntityAlreadyExistException("пользователь с email: " + user.getEmail());
        }
        user.setId(++id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user, Long userId) {
        User userFound = findById(userId);
        if (isUserExist(user.getEmail()) && !user.getEmail().equals(userFound.getEmail())) {
            throw new EntityAlreadyExistException("пользователь с email: " + user.getEmail() + " email занят, невозможно изменить");
        }
        users.remove(userId);
        User updatedUser = composeFieldsForUpdate(userFound, user);
        users.put(userId, updatedUser);
        return updatedUser;
    }

    private User composeFieldsForUpdate(User user, User userUpdated) {
        User userResult = User.builder()
                .id(user.getId())
                .email(userUpdated.getEmail() != null ? userUpdated.getEmail() : user.getEmail())
                .name(userUpdated.getName() != null ? userUpdated.getName() : user.getName())
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
