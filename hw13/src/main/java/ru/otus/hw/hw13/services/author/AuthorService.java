package ru.otus.hw.hw13.services.author;

import ru.otus.hw.hw13.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
