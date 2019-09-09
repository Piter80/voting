package ru.zimin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.zimin.model.Dish;
import ru.zimin.service.DishService;

import java.util.List;


@RestController
@RequestMapping(value = AdminDishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController {

    static final String REST_URL = "/rest/admin/dish";

    private final DishService service;

    @Autowired
    public AdminDishRestController(DishService service) {
        this.service = service;
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseStatus(value = HttpStatus.OK)
    public Dish get(@PathVariable("id") int id) {
        return service.get(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        service.delete(id);
    }


    @GetMapping(value = "/restaurant/{restId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Dish> getAllByRest(@PathVariable("restId") int restId){
        return service.getAll(restId);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Dish update(@RequestBody Dish dish, @PathVariable("id") int id, @RequestParam("restId") int restId) {
        return service.update(dish, id, restId);
    }

    @PutMapping(value = "/{restId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Dish save(@RequestBody Dish dish, @PathVariable("restId") int restId) {
        return service.create(dish, restId);
    }




}
