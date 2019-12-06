package ru.feud.admin.rest.ro;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class DeleteRequest {

    @NotNull
    private Long id;

}
