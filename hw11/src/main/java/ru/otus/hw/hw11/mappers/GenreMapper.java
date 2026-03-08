package ru.otus.hw.hw11.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11.dto.GenreDto;
import ru.otus.hw.hw11.models.Genre;

@Component
public class GenreMapper {
    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
