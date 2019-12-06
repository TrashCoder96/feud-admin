package ru.feud.admin.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.feud.admin.rest.ro.ErrorMessageRo;

@ControllerAdvice
public class RestAdvice {

    @ExceptionHandler(FeudRestException.class)
    public ResponseEntity<ErrorMessageRo> feudException(FeudRestException ex) {
        return ResponseEntity.status(ex.getHttpStatus().value())
                .body(new ErrorMessageRo().setStatus(ex.getHttpStatus().value()).setMessage(ex.getMessage()));
    }

}
