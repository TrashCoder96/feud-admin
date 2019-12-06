package ru.feud.admin.rest.ro;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorMessageRo {

    private Integer status;
    private String message;

}
