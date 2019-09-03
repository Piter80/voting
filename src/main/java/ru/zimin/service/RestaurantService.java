package ru.zimin.service;

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

    public Restaurant get(int id)  {
        Restaurant restaurant = crudRestaurantRepository.findById(id).orElse(null);
        checkNotFoundWithId(restaurant, id, Restaurant.class);
        return restaurant;
    }

    public void delete(int id) {
        crudRestaurantRepository.deleteById(id);
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll();
    }

    public Restaurant create(Restaurant restaurant) {
        return crudRestaurantRepository.save(restaurant);
    }

    public Restaurant update(Restaurant restaurant, int id) {
        Restaurant updated = crudRestaurantRepository.findById(id).orElse(null);
        checkNotFoundWithId(updated, id, Restaurant.class);
        updated.setName(restaurant.getName());
        return crudRestaurantRepository.save(updated);
    }
}