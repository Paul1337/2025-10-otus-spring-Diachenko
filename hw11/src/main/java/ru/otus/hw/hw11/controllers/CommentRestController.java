package ru.otus.hw.hw11.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw11.dto.CommentDto;
import ru.otus.hw.hw11.dto.CreateCommentDto;
import ru.otus.hw.hw11.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping({ "/books/{id}/comments", "/{id}/comments/" })
    public List<CommentDto> getCommentsForBook(@PathVariable("id") Long id) {
        return commentService.findAllByBookId(id);
    }

    @PostMapping({ "/books/{id}/comments", "/{id}/comments/" })
    public CommentDto addCommentForBook(@PathVariable("id") Long id, @Valid @RequestBody CreateCommentDto dto) {
        return commentService.create(dto.getText(), id);
    }
}
