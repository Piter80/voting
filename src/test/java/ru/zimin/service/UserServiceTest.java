package ru.zimin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.zimin.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zimin.TestData.*;

class UserServiceTest extends AbstractServiceTest {
    @Autowired
    private UserService service;

    @Test
    void getAll() {
        assertMatch(service.getAll(), USER, ADMIN);
    }

    @Test
    void create() {
        service.create(CREATED_USER);
        assertMatch(service.getAll(), USER, ADMIN, CREATED_USER);
    }

    @Test
    void update() {
        service.update(UPDATED_USER);
        assertMatch(service.getAll(), UPDATED_USER, ADMIN);
    }

    @Test
    void updateTo() {
        service.update(UPDATED_USER_TO, 1000);
        assertMatch(service.getAll(), UPDATED_USER, ADMIN);
    }

    @Test
    void delete() {
        service.delete(1000);
        assertEquals(List.of(ADMIN), service.getAll());
    }

    @Test
    void deleteNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.delete(100));
    }

    @Test
    void get() {
        var user = service.get(1000);
        assertThat(user).isEqualToIgnoringGivenFields(USER, "registered", "password");
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(100));
    }

    @Test
    void getByEmail() {
        assertThat(service.getByEmail("user@ya.ru")).isEqualTo(USER);
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("NOTUSER@gmail.com"));
    }


}
