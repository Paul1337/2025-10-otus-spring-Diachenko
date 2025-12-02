package ru.otus.hw.hw06.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.AuthorDto;
import ru.otus.hw.hw06.dto.GenreDto;
import ru.otus.hw.hw06.models.Author;
import ru.otus.hw.hw06.models.Genre;

@Component
public class GenreMapper {
    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
