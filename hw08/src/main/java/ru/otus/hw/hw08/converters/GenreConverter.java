package ru.otus.hw.hw08.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw08.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreDtoToString(GenreDto genreDto) {
        return "Id: %s, Name: %s".formatted(genreDto.getId(), genreDto.getName());
    }
}
