package ru.zimin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "voting")
public class Vote extends AbstractBaseEntity {
    public Vote(Integer id, Restaurant restaurant, User user, LocalDateTime dateTime) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
        this.dateTime = dateTime;
    }

    public Vote(Restaurant restaurant, User user, LocalDateTime dateTime) {
        this.restaurant = restaurant;
        this.user = user;
        this.dateTime = dateTime;
    }

    public Vote() {
    }

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonInclude
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonInclude
    private User user;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote)) return false;
        if (!super.equals(o)) return false;
        Vote vote = (Vote) o;
        return Objects.equals(restaurant, vote.restaurant) &&
                Objects.equals(user, vote.user) &&
                Objects.equals(dateTime, vote.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), restaurant, user, dateTime);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "restaurant=" + restaurant.getName() +
                ", user=" + user.getEmail() +
                ", dateTime=" + dateTime +
                ", vote id=" + id +
                '}';
    }
}
