package ru.zimin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.zimin.model.Dish;
import ru.zimin.model.Restaurant;
import ru.zimin.repository.CrudDishRepository;
import ru.zimin.repository.CrudRestaurantRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.zimin.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {
    @Autowired
    CrudRestaurantRepository restaurantRepository;
    @Autowired
    private CrudDishRepository dishRepository;

    public Dish get(int id) {
        Dish dish = dishRepository.findById(id).orElse(null);
        checkNotFoundWithId(dish, id, Dish.class);
        return dish;
    }

    public void delete(int id) {
        Dish dish = dishRepository.findById(id).orElse(null);
        checkNotFoundWithId(dish, id, Dish.class);
        dishRepository.deleteById(id);
    }

    public List<Dish> getAll(int restId) {
        List<Dish> dishes = dishRepository.getAll(restId).orElse(null);
        Assert.notNull(dishes, "not found dishes with restaurant id " + restId);
        return dishes;
    }

    public Dish create(Dish dish, int restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElse(null);
        checkNotFoundWithId(restaurant, restId, Restaurant.class);
        dish.setRestaurant(restaurant);
        if (dish.getCreated() == null) dish.setCreated(LocalDateTime.now());
        return dishRepository.save(dish);
    }

    public Dish update(Dish dish, int id, int restId) {
        Assert.notNull(dishRepository.findById(id).orElse(null), "dish must not be null when updated");
        Restaurant restaurant = restaurantRepository.findById(restId).orElse(null);
        checkNotFoundWithId(restaurant, restId, Restaurant.class);
        Dish updated = new Dish(id, dish.getName(), restaurant, dish.getPrice(), dish.getCreated());
        return dishRepository.save(updated);

    }

}
