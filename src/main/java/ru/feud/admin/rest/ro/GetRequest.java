package ru.feud.admin.rest.ro;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class GetRequest {

    @NotNull
    private Long id;

}
