package ru.otus.hw.hw12.services;

import ru.otus.hw.hw12.dto.GenreDto;
import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
