package ru.feud.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetOneTest extends BasicTest {

    @BeforeEach
    @Transactional
    public void cleanDb() {
        gameRepository.deleteAll();
    }

    @Test
    @Transactional
    public void getOne_ok() throws Exception {
        Long id = gameRepository.save(validGame().setKey("key123")).getId();
        this.mockMvc.perform(get("/api/v1/game/" + id)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.game").exists());
    }

}
