package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.EntityAlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.helper.Helpers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        return UserMapper.toUserDto(users);
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = repository.save(UserMapper.toNewUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(Long id) {
        Optional<User> userOpt = repository.findById(id);
        userOpt.orElseThrow(() -> new NotFoundException("пользователь с id=" + id));
        return UserMapper.toUserDto(userOpt.get());
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {

        Optional<User> userOpt = repository.findById(userId);
        if (userOpt.isEmpty()) throw new NotFoundException("пользователь с id=" + userId);
        User userFound = userOpt.get();

        if (userDto.getEmail() != null && !userDto.getEmail().equals(userFound.getEmail())) {
            isEmailAvalable(userDto.getEmail());
        }

        User userToUpdate = UserMapper.toUser(userDto);
        User updatedUser = Helpers.composeFieldsForUpdateUser(userFound, userToUpdate);
        User userSaved = repository.save(updatedUser);
        return UserMapper.toUserDto(userSaved);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    private Boolean isEmailAvalable(String email) {
        Optional<User> userOptEmail = repository.findUserByEmail(email);
        if (userOptEmail.isPresent())
            throw new EntityAlreadyExistException("пользователь с email: " + email);
        return true;
    }
}