package ru.otus.hw.hw13.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.hw13.dto.CommentDto;
import ru.otus.hw.hw13.dto.CreateCommentDto;
import ru.otus.hw.hw13.services.comment.CommentService;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping({ "/{id}/comments", "/{id}/comments/" })
    public List<CommentDto> getCommentsForBook(@PathVariable("id") Long id) {
        return commentService.findAllByBookId(id);
    }

    @PostMapping({ "/{id}/comments", "/{id}/comments/" })
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addCommentForBook(@PathVariable("id") Long id, @Valid @RequestBody CreateCommentDto dto) {
        return commentService.create(dto.getText(), id);
    }
}
