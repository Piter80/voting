package ru.zimin.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.zimin.model.User;
import ru.zimin.service.UserService;

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

class AdminUserRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = AdminUserRestController.REST_URL + "/";

    @Autowired
    private UserService service;


    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getUserMatcher(USER, ADMIN));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + USER.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(readFromJsonMvcResult(result, User.class), USER));
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
        mockMvc.perform(delete(REST_URL + USER.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertEquals(List.of(ADMIN), service.getAll());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 100)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post(REST_URL + USER.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(UPDATED_USER)))
                .andExpect(status().isOk());
        assertThat(UPDATED_USER).isEqualToIgnoringGivenFields(service.get(USER.getId()), "password");
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(put(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(CREATED_USER)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertThat(service.getAll()).usingElementComparatorIgnoringFields("password", "registered").isEqualTo(List.of(USER, ADMIN, CREATED_USER));
    }

    @Test
    void getByMail() throws Exception {
        mockMvc.perform(get(REST_URL + "by")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "user@ya.ru"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(USER, readFromJsonMvcResult(result, User.class)));
    }

    @Test
    void getByMailNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "by")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "NOTFOUND@EMAIL.COM"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByBadMail() throws Exception {
        mockMvc.perform(get(REST_URL + "by")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "NOTFOUND"))
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
