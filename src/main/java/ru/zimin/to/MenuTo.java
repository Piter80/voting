package ru.zimin.to;

import ru.zimin.model.Dish;
import ru.zimin.model.Restaurant;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MenuTo implements Serializable {

    private Restaurant restaurant;

    private List<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(Restaurant restaurant, List<Dish> dishes) {
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuTo)) return false;
        MenuTo menuTo = (MenuTo) o;
        return (menuTo.restaurant.getId().equals(this.restaurant.getId()) && menuTo.dishes.equals(((MenuTo) o).dishes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurant, dishes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Restaurant ").append(restaurant.getName()).append(" [id: ]").append(restaurant.getId());
        for(Dish dish: dishes) {
            sb.append(dish.menuString());
        }

        return sb.toString();
    }
}
