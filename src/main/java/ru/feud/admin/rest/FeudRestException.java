package ru.feud.admin.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class FeudRestException extends RuntimeException {

    private HttpStatus httpStatus;

    private String message;

}
