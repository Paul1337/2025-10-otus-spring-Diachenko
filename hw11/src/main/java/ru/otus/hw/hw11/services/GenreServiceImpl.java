package ru.otus.hw.hw11.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11.dto.GenreDto;
import ru.otus.hw.hw11.mappers.GenreMapper;
import ru.otus.hw.hw11.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(genreMapper::genreToDto);
    }

}
