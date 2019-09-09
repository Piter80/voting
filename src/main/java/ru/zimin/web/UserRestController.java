package ru.zimin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.zimin.model.User;
import ru.zimin.model.Vote;
import ru.zimin.service.DishService;
import ru.zimin.service.RestaurantService;
import ru.zimin.service.UserService;
import ru.zimin.service.VoteService;
import ru.zimin.to.MenuTo;
import ru.zimin.to.UserTo;
import ru.zimin.util.MenuUtil;
import ru.zimin.util.UserUtil;
import ru.zimin.util.exception.VotingTimeViolationException;

import java.util.List;

import static ru.zimin.web.SecurityUtil.*;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {
    static final String REST_URL = "/rest/user";

    private final VoteService service;
    private final DishService dishService;
    private final RestaurantService restaurantService;
    private final UserService userService;

    @Autowired
    public UserRestController(VoteService service, DishService dishService, RestaurantService restaurantService, UserService userService) {
        this.service = service;
        this.dishService = dishService;
        this.restaurantService = restaurantService;
        this.userService = userService;
    }


    @GetMapping("/{restId}")
    @ResponseStatus(HttpStatus.OK)
    public Vote vote(@PathVariable("restId") int restId) throws VotingTimeViolationException {
        int userId = authUserId();
        return service.createForUser(restId, userId);
    }

    @GetMapping("/menu/{restId}")
    @ResponseStatus(HttpStatus.OK)
    public MenuTo getMenu(@PathVariable("restId") int restId) {
        return MenuUtil.asTo(restaurantService.get(restId), dishService.getAll(restId));
    }


    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuTo> getMenuToVote() {
        return service.getAllRestaurantsWithMenu();
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public UserTo update(@RequestBody UserTo userTo) {
        return userService.update(userTo, authUserId());
    }


    @PutMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User register(@RequestBody UserTo userTo) {
        return userService.create(UserUtil.createNewFromTo(userTo));
    }
}