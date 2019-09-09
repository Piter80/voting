package ru.zimin.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.zimin.service.DishService;
import ru.zimin.service.RestaurantService;
import ru.zimin.service.VoteService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zimin.TestData.*;
import static ru.zimin.TestUtil.*;
import static ru.zimin.web.json.JsonUtil.*;

@SuppressWarnings({"ALL", "SpringJavaInjectionPointsAutowiringInspection"})
class UserRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = UserRestController.REST_URL + "/";

    @Autowired
    private VoteService service;
    @Autowired
    private DishService dishService;
    @Autowired
    private RestaurantService restaurantService;

    @Test
    void vote() throws Exception {
        mockMvc.perform(get(REST_URL + BREACKINGEAT.getId())
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertDoesNotThrow(() -> service.get(1002));
    }

    @Test
    void voteNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 100)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void voteNotBadArgument() throws Exception {
        mockMvc.perform(get(REST_URL + "BAD")
                .with(userHttpBasic(USER)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMenu() throws Exception {
        mockMvc.perform(get(REST_URL + "menu/" + BREACKINGEAT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMenuNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "menu/" + 100)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMenuBadArgument() throws Exception {
        mockMvc.perform(get(REST_URL + "menu/" + "BAD")
                .with(userHttpBasic(USER)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllRestaurantsWithMenu() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateAsUser() throws Exception {
        mockMvc.perform(post(REST_URL + "update")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(UPDATED_USER_TO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void registerAsUser() throws Exception {
        mockMvc.perform(put(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(CREATED_USER_TO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getUserMatcher(CREATED_USER));
    }

    @Test
    void getUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}
