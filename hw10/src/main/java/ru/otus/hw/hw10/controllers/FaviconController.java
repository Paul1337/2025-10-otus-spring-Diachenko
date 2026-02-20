package ru.otus.hw.hw10.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FaviconController {
    
    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon(HttpServletResponse response) {
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}