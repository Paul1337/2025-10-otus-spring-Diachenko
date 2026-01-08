package ru.otus.hw.hw08.services;

import ru.otus.hw.hw08.dto.BookDto;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(String id);

    List<BookDto> findAll();

    BookDto insert(String title, String authorId, Set<String> genresIds);

    BookDto update(String id, String title, String authorId, Set<String> genresIds);

    void deleteById(String id);
}
