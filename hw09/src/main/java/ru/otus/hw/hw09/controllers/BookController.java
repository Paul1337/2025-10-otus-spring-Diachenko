package ru.otus.hw.hw09.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.hw09.dto.BookDto;
import ru.otus.hw.hw09.dto.CreateBookDto;
import ru.otus.hw.hw09.dto.UpdateBookDto;
import ru.otus.hw.hw09.services.AuthorService;
import ru.otus.hw.hw09.services.BookService;
import ru.otus.hw.hw09.services.CommentService;
import ru.otus.hw.hw09.services.GenreService;

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

    @PostMapping("/new")
    public String createBook(@ModelAttribute("book") CreateBookDto dto) {
        bookService.insert(dto);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        var bookDto = bookService.findById(id);
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/edit";
    }

    @PostMapping("/{id}/edit")
    public String editBook(@PathVariable long id, @ModelAttribute("book") UpdateBookDto dto) {
        bookService.update(dto);
        return "redirect:/books/%d".formatted(id);
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
