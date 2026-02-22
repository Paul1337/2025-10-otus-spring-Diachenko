package ru.otus.hw.hw10.services;

import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.dto.CreateBookDto;
import ru.otus.hw.hw10.dto.UpdateBookDto;
import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(CreateBookDto dto);

    BookDto update(UpdateBookDto dto);

    void deleteById(long id);
}
