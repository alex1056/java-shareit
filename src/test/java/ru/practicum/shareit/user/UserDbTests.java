package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbTests {

    @Autowired
    private EntityManager em;
    @Autowired
    private UserService service;

    UserDto userDto1 = new UserDto(null, "user1@mail.ru", "Пётр");

    UserDto userDto2 = new UserDto(null, "user2@mail.ru", "Василий");
    UserDto userDtoForUpdate = new UserDto(null, null, "Василий upd");

    @Test
    void testSaveUser() {

        service.saveUser(userDto1);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto1.getEmail()).getSingleResult();

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo(userDto1.getName()));
        assertThat(user.getEmail(), equalTo(userDto1.getEmail()));
    }

    @Test
    void testGetAllUsers() {

        service.saveUser(userDto1);
        service.saveUser(userDto2);

        List<UserDto> userDtoList = service.getAllUsers();

        assertThat(userDtoList.size(), equalTo(2));
        assertThat(userDtoList.get(0).getId(), equalTo(1L));
        assertThat(userDtoList.get(0).getName(), equalTo(userDto1.getName()));
        assertThat(userDtoList.get(0).getEmail(), equalTo(userDto1.getEmail()));
    }

    @Test
    void testFindUserById() {

        service.saveUser(userDto1);
        UserDto userDto = service.findUserById(1L);

        assertThat(userDto.getId(), equalTo(1L));
        assertThat(userDto.getName(), equalTo(userDto1.getName()));
        assertThat(userDto.getEmail(), equalTo(userDto1.getEmail()));
    }

    @Test
    void testUpdateUser() {

        service.saveUser(userDto1);
        service.updateUser(userDtoForUpdate, 1L);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 1L).getSingleResult();

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo("Василий upd"));
        assertThat(user.getEmail(), equalTo(userDto1.getEmail()));
    }

    @Test
    void testDeleteUser() {

        service.saveUser(userDto1);
        service.deleteUser(1L);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        List<User> userList = query.setParameter("id", 1L).getResultList();

        assertThat(userList.size(), equalTo(0));
    }
}