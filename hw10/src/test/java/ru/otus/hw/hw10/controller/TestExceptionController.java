package ru.otus.hw.hw10.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw10.exceptions.EntityNotFoundException;

@RestController
@RequestMapping("/test")
class TestExceptionController {

    @GetMapping("/not-found")
    public void notFound() {
        throw new EntityNotFoundException("Entity not found");
    }

    @GetMapping("/error")
    public void error() {
        throw new RuntimeException("Error");
    }
}
