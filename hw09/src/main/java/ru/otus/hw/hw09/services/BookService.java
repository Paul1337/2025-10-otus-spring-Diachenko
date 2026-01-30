package ru.otus.hw.hw09.services;

import ru.otus.hw.hw09.dto.BookDto;
import ru.otus.hw.hw09.dto.CreateBookDto;
import ru.otus.hw.hw09.dto.UpdateBookDto;
import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(CreateBookDto dto);

    BookDto update(UpdateBookDto dto);

    void deleteById(long id);
}
