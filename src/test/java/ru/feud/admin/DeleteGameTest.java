package ru.feud.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteGameTest extends BasicTest {

    @BeforeEach
    @Transactional
    public void cleanDb() {
        gameRepository.deleteAll();
    }

    @Test
    @Transactional
    public void deleteGame_ok() throws Exception {
        Long id = gameRepository.save(validGame().setKey("key123")).getId();
        this.mockMvc.perform(delete("/api/v1/game/" + id)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        assertFalse(gameRepository.findById(1L).isPresent());
    }

}
