package ru.zimin.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "dishes")
public class Dish extends AbstractNamedEntity {
    public Dish() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "added")
    private LocalDateTime created;


    public Dish(String name, Restaurant restaurant, BigDecimal price, LocalDateTime created) {
        this.id = null;
        this.name = name;
        this.restaurant = restaurant;
        this.price = price;
        this.created = created;
    }

    public Dish(Integer id, String name, Restaurant restaurant, BigDecimal price, LocalDateTime created) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Dish dish = (Dish) o;
        return Objects.equals(restaurant, dish.restaurant) &&
                price.compareTo(dish.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), restaurant, price, created);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "restaurant=" + restaurant +
                ", price=" + price.setScale(2, RoundingMode.HALF_EVEN) +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public String menuString() {
        return "Menu: [ name: " + name + ", price: " + price + " ]";
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
