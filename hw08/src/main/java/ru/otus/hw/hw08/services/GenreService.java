package ru.otus.hw.hw08.services;

import ru.otus.hw.hw08.dto.GenreDto;
import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
