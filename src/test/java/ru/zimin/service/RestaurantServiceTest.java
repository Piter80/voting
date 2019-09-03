package ru.zimin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.zimin.model.Restaurant;
import ru.zimin.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.zimin.TestData.*;

class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    private RestaurantService service;

    @Test
    void get() {
        var restaurant = service.get(1000);
        assertNotNull(restaurant);
        assertEquals(restaurant, BREACKINGEAT);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(100));
    }

    @Test
    void delete() {
        service.delete(1000);
        assertEquals(List.of(PIGKING), service.getAll());
    }

    @Test
    void deleteNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.delete(100));
    }

    @Test
    void getAll() {
        assertEquals(List.of(BREACKINGEAT, PIGKING), service.getAll());
    }

    @Test
    void create() {
        service.create(new Restaurant("AddedNew"));
        assertThat(service.getAll()).contains(CREATED_REST);
    }

    @Test
    void update() {
        service.update(UPDATED_REST, 1000);
        assertThat(service.get(1000)).isEqualTo(UPDATED_REST);
    }
}
