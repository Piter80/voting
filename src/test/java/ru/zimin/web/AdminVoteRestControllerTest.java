package ru.zimin.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.zimin.model.Vote;
import ru.zimin.service.VoteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zimin.TestData.*;
import static ru.zimin.TestUtil.*;


class AdminVoteRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = AdminVoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1.getId()).with(userHttpBasic(ADMIN))).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> assertEquals(VOTE1, readFromJsonMvcResult(result, Vote.class)));
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 100).with(userHttpBasic(ADMIN))).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + VOTE1.getId()).with(userHttpBasic(ADMIN))).andExpect(status().isNoContent());
        assertEquals(List.of(VOTE2), service.getAll());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 100).with(userHttpBasic(ADMIN))).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    void getByUser() throws Exception {
        mockMvc.perform(get(REST_URL + "user/" + USER.getId()).with(userHttpBasic(ADMIN))).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> assertEquals(List.of(VOTE2), readListFromJsonMvcResult(result, Vote.class)));
    }

    @Test
    void getByUserNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "user/" + 100).with(userHttpBasic(ADMIN))).andDo(print()).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getDaily() throws Exception {
        Vote dailyVote1 = service.create(new Vote(BREACKINGEAT, USER, LocalDateTime.now()));
        Vote dailyVote2 = service.create(new Vote(PIGKING, ADMIN, LocalDateTime.now()));
        mockMvc.perform(get(REST_URL + "daily").with(userHttpBasic(ADMIN))).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> assertEquals(List.of(dailyVote1, dailyVote2), service.getDaily(LocalDate.now())));
    }

    @Test
    void getRestaurant() throws Exception {
        mockMvc.perform(get(REST_URL + "restaurant/" + BREACKINGEAT.getId()).with(userHttpBasic(ADMIN))).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> assertEquals(List.of(VOTE1), readListFromJsonMvcResult(result, Vote.class)));
    }

    @Test
    void getRestaurantNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "restaurant/" + 100).with(userHttpBasic(ADMIN))).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBetween() throws Exception {
        mockMvc.perform(get(REST_URL + "/between").with(userHttpBasic(ADMIN)).param("startDate", "2019-05-30").param("endDate", "2019-05-31")).andDo(print()).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBetweenIllegalArguments() throws Exception {
        mockMvc.perform(get(REST_URL + "/between").with(userHttpBasic(ADMIN)).param("startDate", "asd").param("endDate", "asd")).andDo(print()).andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBetweenWithUser() throws Exception {
        mockMvc.perform(get(REST_URL + "/between/" + USER.getId()).with(userHttpBasic(ADMIN)).param("startDate", "2019-05-30").param("endDate", "2019-05-31")).andDo(print()).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> assertEquals(List.of(VOTE2), readListFromJsonMvcResult(result, Vote.class)));
    }

    @Test
    void getBetweenUserNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "/between/" + 100).with(userHttpBasic(ADMIN)).param("startDate", "2019-05-30").param("endDate", "2019-05-31")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBetweenIllegalArgumentsWithUser() throws Exception {
        mockMvc.perform(get(REST_URL + "/between/" + USER.getId()).with(userHttpBasic(ADMIN)).param("startDate", "asd").param("endDate", "asd")).andDo(print()).andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL)).andExpect(status().isUnauthorized());
    }

    @Test
    void getNoRights() throws Exception {
        mockMvc.perform(get(REST_URL).with(userHttpBasic(USER))).andExpect(status().isForbidden());
    }

}
