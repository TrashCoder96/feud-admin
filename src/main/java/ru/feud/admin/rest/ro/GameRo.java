package ru.feud.admin.rest.ro;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class GameRo {

    private Long id;

    private String key;

    @Size(min = 1, max = 100)
    private String description;

    private List<QuestionRo> questions;

}
