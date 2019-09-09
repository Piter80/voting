package ru.zimin.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.zimin.model.Dish;
import ru.zimin.service.DishService;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zimin.TestData.*;
import static ru.zimin.TestUtil.*;
import static ru.zimin.web.json.JsonUtil.writeValue;

class AdminDishRestControllerTest extends AbstractRestControllerTest{

    private static final String REST_URL = AdminDishRestController.REST_URL + "/";

    @Autowired
    DishService service;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + DISH1.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(DISH1, readFromJsonMvcResult(result, Dish.class)));
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 100)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH1.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThat(service.getAll(BREACKINGEAT.getId())).isEqualTo(List.of(DISH2, DISH3));
    }

    @Test
    void getAllByRest() throws Exception {
        mockMvc.perform(get(REST_URL + "restaurant/" + BREACKINGEAT.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(readListFromJsonMvcResult(result, Dish.class), List.of(DISH1, DISH2, DISH3)));
    }

    @Test
    void getAllByRestNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "restaurant/" + 100)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    void update() throws Exception {
        Dish updated = UPDATED_DISH;

        mockMvc.perform(post(REST_URL + DISH1.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON).param("restId", BREACKINGEAT.getId().toString())
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertThat(service.get(DISH1.getId())).isEqualToIgnoringGivenFields(UPDATED_DISH, "created");
    }

    @Test
    void updateNotFoundRestaurantId() throws Exception {
        Dish updated = UPDATED_DISH;

        mockMvc.perform(post(REST_URL + DISH1.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON).param("restId", "100")
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void updateNullDish() throws Exception {
        Dish updated = new Dish(null, null, null, null);
        mockMvc.perform(post(REST_URL + DISH1.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON).param("restId", "1000")
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void create() throws Exception {
        mockMvc.perform(put(REST_URL + BREACKINGEAT.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(CREATED_DISH)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertThat(service.getAll(BREACKINGEAT.getId())).isEqualTo(List.of(DISH1, DISH2, DISH3, CREATED_DISH));
    }

    @Test
    void saveNotFoundRestaurantId() throws Exception {
        mockMvc.perform(put(REST_URL + 100)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(CREATED_DISH)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    void getUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNoRights() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }
}
