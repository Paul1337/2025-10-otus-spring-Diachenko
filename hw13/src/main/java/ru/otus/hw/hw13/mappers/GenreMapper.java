package ru.otus.hw.hw13.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw13.dto.GenreDto;
import ru.otus.hw.hw13.models.Genre;

@Component
public class GenreMapper {
    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
