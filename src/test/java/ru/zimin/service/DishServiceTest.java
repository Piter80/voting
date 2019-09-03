package ru.zimin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zimin.model.Dish;
import ru.zimin.util.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.zimin.TestData.*;

class DishServiceTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    void get() {
        var dish = service.get(1000);
        assertNotNull(dish);
        assertEquals(DISH1, dish);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(100));
    }

    @Test
    void delete() {
        service.delete(1000);
        assertEquals(List.of(DISH2, DISH3), service.getAll(1000));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(100));

    }

    @Test
    void getAll() {
        assertEquals(List.of(DISH1, DISH2, DISH3), service.getAll(1000));

    }

    @Test
    void create() {
        service.create(new Dish("CreatedDish", BREACKINGEAT, new BigDecimal("60"), LocalDateTime.of(2015, 5, 30, 10, 0)), 1000);
        assertThat(service.getAll(1000)).contains(CREATED_DISH);
    }

    @Test
    void update() {
        service.update(UPDATED_DISH, 1000, 1000);
        assertThat(service.get(1000)).isEqualToIgnoringGivenFields(UPDATED_DISH, "created");

    }


}