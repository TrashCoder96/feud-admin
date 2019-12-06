package ru.feud.admin.rest.ro;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class QuestionRo {

    @Size(min = 1, max = 100)
    private String text;

    private List<AnswerRo> answers;

}
