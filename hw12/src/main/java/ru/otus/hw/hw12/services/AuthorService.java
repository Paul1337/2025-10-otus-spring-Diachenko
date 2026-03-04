package ru.otus.hw.hw12.services;

import ru.otus.hw.hw12.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
