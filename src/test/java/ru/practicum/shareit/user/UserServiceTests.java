package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.EntityAlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockUserRepository;
    private final User user1 = new User(
            1L,
            "user1@mail.ru",
            "Пользователь 1");

    private final User user2 = new User(
            2L,
            "user2@mail.ru",
            "Пользователь 2");

    private final UserDto userDto = new UserDto(
            1L,
            "user1@mail.ru",
            "Пользователь 1");

    private final Optional<User> userOptional = Optional.of(new User(
            1L,
            "user1@mail.ru",
            "Пользователь 1"));

    private final UserDto userDtoForUpdate = new UserDto(
            null,
            "user2@mail.ru",
            "Пользователь 1 upd");

    private final User userForUpdate = new User(
            1L,
            "user2@mail.ru",
            "Пользователь 1 upd");

    @Test
    void testGetAllUsers() {
        UserService userService = new UserServiceImpl(mockUserRepository);
        List<User> userList = List.of(user1, user2);

        Mockito
                .when(mockUserRepository.findAllByOrderByIdAsc())
                .thenReturn(userList);

        List<UserDto> userListDto = userService.getAllUsers();
        Assertions.assertEquals(2, userListDto.size());
        Assertions.assertEquals(1, userListDto.get(0).getId());
        Assertions.assertEquals("user1@mail.ru", userListDto.get(0).getEmail());
        Assertions.assertEquals("Пользователь 1", userListDto.get(0).getName());
        Assertions.assertEquals(2, userListDto.get(1).getId());
        Assertions.assertEquals("user2@mail.ru", userListDto.get(1).getEmail());
        Assertions.assertEquals("Пользователь 2", userListDto.get(1).getName());
    }

    @Test
    void testSaveUser() {
        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.save(Mockito.<User>any()))
                .thenReturn(user1);

        UserDto userDtoResult = userService.saveUser(userDto);
        Assertions.assertEquals(1, userDtoResult.getId());
        Assertions.assertEquals("user1@mail.ru", userDtoResult.getEmail());
        Assertions.assertEquals("Пользователь 1", userDtoResult.getName());
    }

    @Test
    void testFindUserById() {
        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(userOptional);

        UserDto userDtoResult = userService.findUserById(1L);
        Assertions.assertEquals(1, userDtoResult.getId());
        Assertions.assertEquals("user1@mail.ru", userDtoResult.getEmail());
        Assertions.assertEquals("Пользователь 1", userDtoResult.getName());
    }

    @Test
    void testFindUserByIdException() {
        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("пользователь с id=1"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findUserById(1L));

        Assertions.assertEquals("пользователь с id=1", exception.getMessage());
    }

    @Test
    void testSaveMethodInUpdateUser() {
        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(userOptional);

        Mockito
                .when(mockUserRepository.save(userForUpdate))
                .thenReturn(userForUpdate);

        UserDto userDtoResult = userService.updateUser(userDtoForUpdate, 1L);
        Assertions.assertEquals(1, userDtoResult.getId());
        Assertions.assertEquals("user2@mail.ru", userDtoResult.getEmail());
        Assertions.assertEquals("Пользователь 1 upd", userDtoResult.getName());
    }

    @Test
    void testSaveMethodInUpdateUserExists() {
        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(userOptional);

        Mockito
                .when(mockUserRepository.findUserByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(userForUpdate));

        final EntityAlreadyExistException exception = Assertions.assertThrows(
                EntityAlreadyExistException.class,
                () -> userService.updateUser(userDtoForUpdate, 1L));

        Assertions.assertEquals("пользователь с email: user2@mail.ru", exception.getMessage());
    }
}