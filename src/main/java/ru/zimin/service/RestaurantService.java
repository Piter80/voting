package ru.zimin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.zimin.model.Restaurant;
import ru.zimin.repository.CrudRestaurantRepository;

import java.util.List;

import static ru.zimin.util.ValidationUtil.checkNotFoundWithId;


@Service
public class RestaurantService {
    @Autowired
    private CrudRestaurantRepository crudRestaurantRepository;

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    public Restaurant get(int id) {
        log.info("get restaurant with id {}", id);
        Restaurant restaurant = crudRestaurantRepository.findById(id).orElse(null);
        checkNotFoundWithId(restaurant, id, Restaurant.class);
        return restaurant;
    }

    @CacheEvict(value = "vote", allEntries = true)
    public void delete(int id) {
        log.info("delete restaurant with id {}", id);
        crudRestaurantRepository.deleteById(id);
    }

    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return crudRestaurantRepository.findAll();
    }

    @CacheEvict(value = "vote", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create new restaurant with id {}", restaurant.getId());
        return crudRestaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "vote", allEntries = true)
    public Restaurant update(Restaurant restaurant, int id) {
        log.info("update restaurant with id {}", id);
        Restaurant updated = crudRestaurantRepository.findById(id).orElse(null);
        checkNotFoundWithId(updated, id, Restaurant.class);
        updated.setName(restaurant.getName());
        return crudRestaurantRepository.save(updated);
    }
}