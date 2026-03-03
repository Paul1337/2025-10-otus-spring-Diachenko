package ru.otus.hw.hw11.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.GenreDto;

import java.util.List;
import java.util.Set;

public interface GenreService {
    Flux<GenreDto> findAll();
}
