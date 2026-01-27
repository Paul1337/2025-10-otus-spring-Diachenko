package ru.otus.hw.hw09.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw09.dto.GenreDto;
import ru.otus.hw.hw09.models.Genre;

@Component
public class GenreMapper {
    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
