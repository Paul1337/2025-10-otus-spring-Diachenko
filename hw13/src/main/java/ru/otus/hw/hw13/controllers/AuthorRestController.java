package ru.otus.hw.hw13.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw13.dto.AuthorDto;
import ru.otus.hw.hw13.services.author.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping({ "", "/" })
    public List<AuthorDto> listAuthors() {
        return authorService.findAll();
    }
}
