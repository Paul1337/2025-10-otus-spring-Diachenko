package ru.otus.hw.hw11.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.GenreDto;
import ru.otus.hw.hw11.services.GenreService;
import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreService genreService;

    @GetMapping({ "", "/" })
    public Mono<List<GenreDto>> listGenres() {
        return genreService.findAll().collectList();
    }
}
