package ru.otus.hw.hw09.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw09.dto.AuthorDto;
import ru.otus.hw.hw09.mappers.AuthorMapper;
import ru.otus.hw.hw09.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorDto> findAll() {
        var authors = authorRepository.findAll();
        return authors.stream().map(authorMapper::authorToDto).collect(Collectors.toList());
    }
}
