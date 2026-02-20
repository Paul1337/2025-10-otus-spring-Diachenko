package ru.otus.hw.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    @GetMapping({ "", "/" })
    public String listGenres(Model model) {
        return "genres";
    }
}
