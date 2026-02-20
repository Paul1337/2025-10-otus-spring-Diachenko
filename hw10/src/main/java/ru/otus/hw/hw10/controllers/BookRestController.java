package ru.otus.hw.hw10.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.dto.CommentDto;
import ru.otus.hw.hw10.dto.CreateBookDto;
import ru.otus.hw.hw10.dto.UpdateBookDto;
import ru.otus.hw.hw10.services.BookService;
import ru.otus.hw.hw10.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping({ "/{id}", "/{id}/" })
    public BookDto getBookById(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }

    @GetMapping({ "/{id}/comments", "/{id}/comments/" })
    public List<CommentDto> getCommentsForBook(@PathVariable("id") Long id) {
        return commentService.findAllByBookId(id);
    }

    @GetMapping({ "", "/" })
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @PostMapping({ "", "/" })
    public BookDto createBook(@Valid @RequestBody CreateBookDto dto) {
        return bookService.insert(dto);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public BookDto editBook(@Valid @RequestBody UpdateBookDto dto, @PathVariable("id") Long id) {
        dto.setId(id);
        return bookService.update(dto);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
