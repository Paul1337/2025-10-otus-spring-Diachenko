package ru.otus.hw.hw13.services.genre;

import ru.otus.hw.hw13.dto.GenreDto;
import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
