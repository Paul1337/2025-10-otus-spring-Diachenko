package ru.otus.hw.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    @GetMapping({ "", "/" })
    public String listBooks(Model model) {
        return "books/books";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "books/book";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("id", 0);
        return "books/edit";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "books/edit";
    }
}
