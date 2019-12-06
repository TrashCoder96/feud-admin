package ru.feud.admin;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.feud.admin.rest.ro.GameRo;
import ru.feud.admin.rest.ro.UpdateRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateGameTest extends BasicTest {

    @Test
    public void updateGame_ok() throws Exception {
        gameRepository.save(validGame().setId(1L).setKey("key123"));
        this.mockMvc.perform(put("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(new UpdateRequest().setGame(validGameRo()
                        .setId(1L)
                        .setDescription("newdesc")))))
                .andExpect(status().isOk()).andReturn();
        assertEquals("newdesc", objectMapper.readValue(gameRepository.getOne(1L).getBody(), GameRo.class).getDescription());
    }

}
