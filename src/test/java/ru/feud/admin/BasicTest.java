package ru.feud.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.feud.admin.data.GameRepository;
import ru.feud.admin.data.dto.Game;
import ru.feud.admin.rest.ro.AnswerRo;
import ru.feud.admin.rest.ro.GameRo;
import ru.feud.admin.rest.ro.QuestionRo;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class BasicTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected GameRepository gameRepository;

    public Game validGame() throws JsonProcessingException {
        return new Game().setBody(objectMapper.writeValueAsString(validGameRo()));
    }

    public GameRo validGameRo() {
        GameRo gameRo = new GameRo()
            .setDescription("desc");
        QuestionRo questionRo = new QuestionRo()
            .setText("quest");
        AnswerRo answerRo = new AnswerRo()
            .setText("answer")
            .setScore(10);
        gameRo.setQuestions(new ArrayList<>());
        gameRo.getQuestions().add(questionRo);
        questionRo.setAnswers(new ArrayList<>());
        questionRo.getAnswers().add(answerRo);
        return gameRo;
    }

}
