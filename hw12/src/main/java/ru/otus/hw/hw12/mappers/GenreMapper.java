package ru.otus.hw.hw12.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw12.dto.GenreDto;
import ru.otus.hw.hw12.models.Genre;

@Component
public class GenreMapper {
    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
