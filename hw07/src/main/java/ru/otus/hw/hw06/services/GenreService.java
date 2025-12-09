package ru.otus.hw.hw06.services;

import ru.otus.hw.hw06.dto.GenreDto;
import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
