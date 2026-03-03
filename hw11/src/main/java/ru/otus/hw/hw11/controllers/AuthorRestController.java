package ru.otus.hw.hw11.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.AuthorDto;
import ru.otus.hw.hw11.services.AuthorService;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping({ "", "/" })
    public Mono<List<AuthorDto>> listAuthors() {
        return authorService.findAll().collectList();
    }
}
