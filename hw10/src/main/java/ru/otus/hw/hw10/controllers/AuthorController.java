package ru.otus.hw.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    @GetMapping({ "", "/" })
    public String listAuthors(Model model) {
        return "authors";
    }
}
