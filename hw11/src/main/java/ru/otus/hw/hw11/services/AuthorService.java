package ru.otus.hw.hw11.services;

import ru.otus.hw.hw11.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
