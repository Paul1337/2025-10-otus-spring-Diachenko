package ru.otus.hw.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.services.AuthorService;
import ru.otus.hw.hw10.services.BookService;
import ru.otus.hw.hw10.services.CommentService;
import ru.otus.hw.hw10.services.GenreService;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping({ "", "/" })
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books/books";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable long id, Model model) {
        var bookDto = bookService.findById(id);
        var commentDtos = commentService.findAllByBookId(id);
        model.addAttribute("book", bookDto);
        model.addAttribute("comments", commentDtos);
        return "books/book";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/edit";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        var bookDto = bookService.findById(id);
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/edit";
    }
}
