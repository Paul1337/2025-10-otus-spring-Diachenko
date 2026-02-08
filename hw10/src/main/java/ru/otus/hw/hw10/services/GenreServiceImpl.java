package ru.otus.hw.hw10.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw10.dto.GenreDto;
import ru.otus.hw.hw10.mappers.GenreMapper;
import ru.otus.hw.hw10.repositories.GenreRepository;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> findAll() {
        var genres = genreRepository.findAll();
        return genres.stream().map(genreMapper::genreToDto).collect(Collectors.toList());
    }
}
