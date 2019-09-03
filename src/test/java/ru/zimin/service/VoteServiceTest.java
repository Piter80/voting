package ru.zimin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.zimin.model.Vote;
import ru.zimin.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.zimin.TestData.*;

class VoteServiceTest extends AbstractServiceTest {

    private final VoteService service;

    @Autowired
    public VoteServiceTest(VoteService service) {
        this.service = service;
    }


    @Test
    void get() {
        var vote = service.get(1000);
        assertNotNull(vote);
        assertThat(vote).isEqualToIgnoringGivenFields(VOTE1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(100));
    }


    @Test
    void delete() {
        service.delete(1000);
        assertEquals(List.of(VOTE2), service.getAll());
    }

    @Test
    void deleteNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.delete(100));
    }

    @Test
    void getDaily() {
        var dailyVotes = service.getDaily( LocalDate.of(2019, 5, 30));
        assertThat(dailyVotes).contains(VOTE1, VOTE2);
    }

    @Test
    void getRestaurant() {
        var restVotes = service.getRestaurant(1000);
        assertThat(restVotes).doesNotContain(VOTE2).contains(VOTE1);
    }

    @Test
    void getUser() {
        var userVotes = service.getUser(1000);
        assertThat(userVotes).doesNotContain(VOTE1).contains(VOTE2);
    }

    @Test
    void getAll() {
        var allVotes = service.getAll();
        assertEquals(List.of(VOTE1, VOTE2), allVotes);
    }

    @Test
    void getBetweenDates() {
        var between = service.getBetweenDates( LocalDate.of(2019, 5, 30),  LocalDate.of(2019, 5, 30));
        assertEquals(List.of(VOTE1, VOTE2), between);
    }

    @Test
    void getBetweenDatesWithUser() {
        var betweenWithUser = service.getBetweenDatesWithUser(1000,  LocalDate.of(2018, 5, 30),  LocalDate.of(2019, 12, 30));
        assertThat(betweenWithUser).contains(VOTE2);
    }

    @Test
    void update() {
        service.update(UPDATED_VOTE, 1000);
        assertThat(service.get(1000)).isEqualToIgnoringGivenFields(UPDATED_VOTE, "dateTime");
    }

    @Test
    void create() {
        service.create(new Vote(BREACKINGEAT, USER,  LocalDateTime.of(2019, 5, 30, 10, 0)));
        assertThat(service.getAll()).usingElementComparatorIgnoringFields("roles").contains(CREATED_VOTE);
    }

}
