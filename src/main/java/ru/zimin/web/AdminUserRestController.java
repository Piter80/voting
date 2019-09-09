package ru.zimin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.zimin.model.User;
import ru.zimin.service.UserService;

import java.util.List;

import static ru.zimin.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(AdminUserRestController.REST_URL)
public class AdminUserRestController {
    static final String REST_URL = "/rest/admin/user";
    @Autowired
    private UserService service;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return service.getAll();
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable("id") int id) {
        return service.get(id);
    }


    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return service.create(user);
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        service.delete(id);
    }


    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User update(@RequestBody User user, @PathVariable("id") int id) {
        assureIdConsistent(user, id);
        return service.update(user);
    }


    @GetMapping(value = "/by", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User getByMail(@RequestParam("email") String email) {
        return service.getByEmail(email);
    }
}