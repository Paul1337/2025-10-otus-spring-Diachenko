package ru.otus.hw.hw09.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw09.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreDtoToString(GenreDto genreDto) {
        return "Id: %d, Name: %s".formatted(genreDto.getId(), genreDto.getName());
    }
}
