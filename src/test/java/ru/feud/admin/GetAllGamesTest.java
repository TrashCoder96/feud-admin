package ru.feud.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetAllGamesTest extends BasicTest {

    @BeforeEach
    @Transactional
    public void cleanDb() {
        gameRepository.deleteAll();
    }

    @Test
    @Transactional
    public void getAllGames_ok() throws Exception {
        Long id = gameRepository.save(validGame().setKey("key123")).getId();
        this.mockMvc.perform(get("/api/v1/games")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.games").exists())
            .andExpect(jsonPath("$.games", hasSize(1)));
    }

}
