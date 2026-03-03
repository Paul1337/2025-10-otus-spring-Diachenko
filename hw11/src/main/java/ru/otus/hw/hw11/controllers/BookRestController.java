package ru.otus.hw.hw11.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.BookDto;
import ru.otus.hw.hw11.dto.CreateBookDto;
import ru.otus.hw.hw11.dto.UpdateBookDto;
import ru.otus.hw.hw11.services.BookService;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @GetMapping({ "/{id}", "/{id}/" })
    public Mono<BookDto> getBookById(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @GetMapping({ "", "/" })
    public Flux<BookDto> getAll() {
        return bookService.findAll();
    }

    @PostMapping({ "", "/" })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> createBook(@Valid @RequestBody CreateBookDto dto) {
        return bookService.insert(dto);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public Mono<BookDto> editBook(@Valid @RequestBody UpdateBookDto dto, @PathVariable("id") String id) {
        dto.setId(id);
        return bookService.update(dto);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable String id) {
        return bookService.deleteById(id);
    }
}
