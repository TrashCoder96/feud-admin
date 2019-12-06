package ru.feud.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.feud.admin.rest.ro.GameRo;
import ru.feud.admin.rest.ro.UpdateRequest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateGameTest extends BasicTest {

    @BeforeEach
    @Transactional
    public void cleanDb() {
        gameRepository.deleteAll();
    }

    @Test
    @Transactional
    public void updateGame_ok() throws Exception {
        Long id = gameRepository.save(validGame().setKey("key123")).getId();
        this.mockMvc.perform(put("/api/v1/game")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(new UpdateRequest().setGame(validGameRo()
                .setId(id)
                .setDescription("newdesc")))))
            .andExpect(status().isOk());
        assertEquals("newdesc", objectMapper.readValue(gameRepository.getOne(1L).getBody(), GameRo.class).getDescription());
    }

}
