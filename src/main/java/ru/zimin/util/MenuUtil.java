package ru.zimin.util;

import ru.zimin.model.Dish;
import ru.zimin.model.Restaurant;
import ru.zimin.to.MenuTo;

import java.util.List;

public class MenuUtil {

    private MenuUtil() {
    }

    public static MenuTo asTo(Restaurant restaurant, List<Dish> dishes) {
        MenuTo menu = new MenuTo();
        menu.setRestaurant(restaurant);
        menu.setDishes(dishes);
        return menu;
    }
}