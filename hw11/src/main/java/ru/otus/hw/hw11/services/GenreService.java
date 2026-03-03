package ru.otus.hw.hw11.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.hw11.dto.GenreDto;

public interface GenreService {
    Flux<GenreDto> findAll();
}
