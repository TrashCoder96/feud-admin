package ru.feud.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.feud.admin.rest.ro.CreateRequest;
import ru.feud.admin.rest.ro.OneGameResponse;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateGameTest extends BasicTest {

    @BeforeEach
    @Transactional
    public void cleanDb() {
        gameRepository.deleteAll();
    }

    @Test
    public void createGame_ok() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/v1/game")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(new CreateRequest().setGame(validGameRo()))))
            .andExpect(status().isOk()).andReturn();
        OneGameResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), OneGameResponse.class);
        assertNotNull(gameRepository.getOne(response.getGame().getId()));
    }

}
