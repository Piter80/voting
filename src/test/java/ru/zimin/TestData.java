package ru.zimin;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.zimin.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.zimin.TestUtil.readFromJsonMvcResult;
import static ru.zimin.TestUtil.readListFromJsonMvcResult;

public final class TestData {
    public static final Restaurant BREACKINGEAT = new Restaurant(1000, "Breaking eat");
    public static final Restaurant PIGKING = new Restaurant(1001, "PigKing");
    public static final Restaurant CREATED_REST = new Restaurant(1002, "AddedNew");
    public static final Restaurant UPDATED_REST = new Restaurant(BREACKINGEAT.getId(), "UpdatedRest");
    

    public static final Dish DISH1 = new Dish(1000, "Eat and break", BREACKINGEAT, new BigDecimal("25.39"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static final Dish DISH2 = new Dish(1001, "Go-go in toilet", BREACKINGEAT, new BigDecimal("45.51"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static final Dish DISH3 = new Dish(1002, "Omnomnom", BREACKINGEAT, new BigDecimal("60.99"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static Dish DISH4 = new Dish(1003, "PigInSoil", PIGKING, new BigDecimal("24.39"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static Dish DISH5 = new Dish(1004, "PiggerSwin", PIGKING, new BigDecimal("44.51"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static Dish DISH6 = new Dish(1005, "Fat Pa", PIGKING, new BigDecimal("59.99"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static final Dish CREATED_DISH = new Dish(1006, "CreatedDish", BREACKINGEAT, new BigDecimal("60"), LocalDateTime.of(2019, 5, 30, 10, 0));
    public static final Dish UPDATED_DISH = new Dish(DISH1.getId(), "UpdatedBreakfast", DISH1.getRestaurant(), DISH1.getPrice(), DISH1.getCreated());

    private TestData() {
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "password");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "password").isEqualTo(expected);
    }

    public static ResultMatcher getUserMatcher(User... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, User.class), List.of(expected));
    }

    public static ResultMatcher getUserMatcher(User expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, User.class), expected);
    }
}