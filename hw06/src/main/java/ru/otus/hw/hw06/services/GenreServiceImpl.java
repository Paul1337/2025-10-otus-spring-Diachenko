package ru.otus.hw.hw06.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw06.converters.GenreConverter;
import ru.otus.hw.hw06.dto.GenreDto;
import ru.otus.hw.hw06.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @Override
    public List<GenreDto> findAll() {
        var genres = genreRepository.findAll();
        return genres.stream().map(genreConverter::genreToDto).collect(Collectors.toList());
    }
}
