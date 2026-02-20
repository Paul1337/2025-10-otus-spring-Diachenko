package ru.otus.hw.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw10.dto.GenreDto;
import ru.otus.hw.hw10.services.GenreService;
import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreService genreService;

    @GetMapping({ "", "/" })
    public List<GenreDto> listGenres() {
        return genreService.findAll();
    }
}
