package ru.otus.hw.hw11.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.AuthorDto;
import ru.otus.hw.hw11.mappers.AuthorMapper;
import ru.otus.hw.hw11.repositories.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .map(authorMapper::authorToDto);
    }

    @Override
    public Mono<AuthorDto> findById(String id) {
        return authorRepository.findById(id)
                .map(authorMapper::authorToDto);
    }


}
