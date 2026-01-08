package ru.otus.hw.hw08.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.hw08.converters.GenreConverter;
import ru.otus.hw.hw08.services.GenreService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {
    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
