package ru.otus.hw.hw06.services;

import ru.otus.hw.hw06.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
