package ru.otus.hw.hw11.services;

import ru.otus.hw.hw11.dto.GenreDto;
import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
