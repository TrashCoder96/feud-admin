package ru.feud.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.feud.admin.data.GameRepository;
import ru.feud.admin.rest.ro.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateGameTest extends BasicTest {

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
