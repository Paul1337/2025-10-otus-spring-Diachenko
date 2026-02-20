package ru.otus.hw.hw10.controllers.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.hw10.dto.ErrorDto;
import ru.otus.hw.hw10.exceptions.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handeEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorDto("Not found");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Exception ex) {
        log.error("", ex);
        return new ErrorDto("Internal server error");
    }
}
