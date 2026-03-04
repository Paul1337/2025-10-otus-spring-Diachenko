package ru.otus.hw.hw12.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw12.dto.AuthorDto;
import ru.otus.hw.hw12.services.AuthorService;

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
