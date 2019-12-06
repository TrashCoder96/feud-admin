package ru.feud.admin.rest.ro;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
public class AnswerRo {

    @Size(min = 1, max = 100)
    private String text;

    @Min(0)
    @NotNull
    private Integer score;

}
