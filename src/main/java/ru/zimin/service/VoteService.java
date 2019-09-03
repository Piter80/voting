package ru.zimin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.zimin.model.Restaurant;
import ru.zimin.model.User;
import ru.zimin.model.Vote;
import ru.zimin.repository.CrudDishRepository;
import ru.zimin.repository.CrudRestaurantRepository;
import ru.zimin.repository.CrudUserRepository;
import ru.zimin.repository.CrudVoteRepository;
import ru.zimin.to.MenuTo;
import ru.zimin.util.MenuUtil;
import ru.zimin.util.exception.IllegalRequestDataException;
import ru.zimin.util.exception.VotingTimeViolationException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.zimin.util.ValidationUtil.*;

@Service
public class VoteService{
    @Autowired
    private CrudVoteRepository crudVoteRepository;
    @Autowired
    private CrudRestaurantRepository crudRestaurantRepository;
    @Autowired
    private CrudDishRepository crudDishRepository;
    @Autowired
    private CrudUserRepository crudUserRepository;

    public Vote update(Vote vote, int id) {
        Vote toUpdate = crudVoteRepository.findById(vote.getId()).orElse(null);
        Assert.notNull(toUpdate, "vote must not be null");
        toUpdate.setUser(vote.getUser());
        toUpdate.setRestaurant(vote.getRestaurant());
        toUpdate.setDateTime(LocalDateTime.now());
        return crudVoteRepository.save(toUpdate);
    }

    public Vote create(Vote vote) {
        if (vote.getDateTime() == null) {
            vote.setDateTime(LocalDateTime.now());
        }
        return crudVoteRepository.save(vote);
    }

    public Vote createForUser(int restId, int userId) {
        List<Vote> votesToday = getBetweenDatesWithUserUtil(1000, LocalDate.now(), LocalDate.now());
        if (votesToday.isEmpty()) {
            Restaurant restaurant = crudRestaurantRepository.findById(restId).orElse(null);
            checkNotFoundWithId(restaurant, restId, Restaurant.class);
            User user = crudUserRepository.findById(userId).orElse(null);
            Vote vote = new Vote();
            vote.setDateTime(LocalDateTime.now());
            vote.setRestaurant(restaurant);
            vote.setUser(user);
            return crudVoteRepository.save(vote);
        }
        if (LocalTime.now().isBefore(LocalTime.of(11, 0))) {
            if (votesToday.get(0).getRestaurant().getId() != restId) {
                Restaurant restaurant = crudRestaurantRepository.findById(restId).orElse(null);
                checkNotFoundWithId(restaurant, restId, Restaurant.class);
                User user = crudUserRepository.findById(userId).orElse(null);
                int idToUpdate = votesToday.get(0).getId();
                Vote toUpdate = crudVoteRepository.findById(idToUpdate).orElse(null);
                Assert.notNull(toUpdate, "can't find vote for update with id: " + idToUpdate);
                Objects.requireNonNull(toUpdate).setUser(user);
                toUpdate.setRestaurant(restaurant);
                toUpdate.setDateTime(LocalDateTime.now());
                return crudVoteRepository.save(toUpdate);
            } else throw new IllegalRequestDataException("you have been already voted for restaurant id: " + restId);
        } else throw new VotingTimeViolationException("you can change your daily vote only before 11:00");
    }

    public void delete(int id) {
        crudVoteRepository.deleteById(id);
    }

    public Vote get(int id) {
        Vote vote = crudVoteRepository.findById(id).orElse(null);
        checkNotFoundWithId(vote,  id, Vote.class);
        return vote;
    }

    public List<Vote> getDaily(LocalDate localDate) {
        List<Vote> votes = crudVoteRepository.getAllBetweenDate(LocalDateTime.of(localDate, LocalTime.MIN), LocalDateTime.of(localDate, LocalTime.MAX)).orElse(null);
        Assert.notNull(votes, "can't find daily votes of: " + localDate);
        return votes;
    }

    public List<Vote> getRestaurant(int restId) {
        List<Vote> votes = crudVoteRepository.getAllByRestaurantId(restId).orElse(null);
        Assert.notNull(votes, "not found votes for restaurant id: " + restId);
        return votes;
    }

    public List<Vote> getUser(int userId) {
        List<Vote> votes = crudVoteRepository.getAllByUserId(userId).orElse(null);
        Assert.notNull(votes, "not found votes for user id: " + userId);
        return votes;
    }

    public List<Vote> getAll() {
        return crudVoteRepository.findAll();
    }

    public List<Vote> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Vote> votes = crudVoteRepository.getAllBetweenDate(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX)).orElse(null);
        Assert.notNull(votes, "not found votes for period from [ " + startDate + " ] to [ " + endDate + " ]" );
        return votes;
    }

    public List<Vote> getBetweenDatesWithUser(int userId, LocalDate startDate, LocalDate endDate) {
        List<Vote> votes = crudVoteRepository.getAllBetweenDateWithUserId(userId, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX)).orElse(null);
        Assert.notNull(votes, "not found votes for period from [ " + startDate + " ] to [ " + endDate + " ] and user id : " + userId );
        return votes;
    }

    
    public List<Vote> getBetweenDatesWithUserUtil(int userId, LocalDate startDate, LocalDate endDate) {
        return crudVoteRepository.getAllBetweenDateWithUserIdUtil(userId, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public List<MenuTo> getAllRestaurantsWithMenu() {
        List<MenuTo> listMenu = new ArrayList<>();
        var restaurants = crudRestaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            listMenu.add(MenuUtil.asTo(restaurant, crudDishRepository.getAll(restaurant.getId()).orElse(null)));
        }
        return listMenu;
    }
}
