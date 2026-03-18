package ru.otus.hw.hw13.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
class TestExceptionController {
    @GetMapping("/error")
    public void error() {
        throw new RuntimeException("Error");
    }
}
