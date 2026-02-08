package ru.otus.hw.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.dto.CreateBookDto;
import ru.otus.hw.hw10.dto.UpdateBookDto;
import ru.otus.hw.hw10.services.BookService;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @PostMapping({ "", "/" })
    public BookDto createBook(@RequestBody CreateBookDto dto) {
        return bookService.insert(dto);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public BookDto editBook(@RequestBody UpdateBookDto dto, @PathVariable("id") Long id) {
        dto.setId(id);
        return bookService.update(dto);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
