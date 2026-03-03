package ru.otus.hw.hw11.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.BookDto;
import ru.otus.hw.hw11.dto.CreateBookDto;
import ru.otus.hw.hw11.dto.UpdateBookDto;
import java.util.List;

public interface BookService {
    Mono<BookDto> findById(String id);

    Flux<BookDto> findAll();

    Mono<BookDto> insert(CreateBookDto dto);

    Mono<BookDto> update(UpdateBookDto dto);

    Mono<Void> deleteById(String id);
}
