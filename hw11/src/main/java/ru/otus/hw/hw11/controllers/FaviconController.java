package ru.otus.hw.hw11.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Controller
public class FaviconController {
    @GetMapping("favicon.ico")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> returnNoFavicon() {
        return Mono.empty();
    }
}