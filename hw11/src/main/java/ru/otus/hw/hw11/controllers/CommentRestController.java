package ru.otus.hw.hw11.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public Mono<List<CommentDto>> getCommentsForBook(@PathVariable("id") String id) {
        return commentService.findAllByBookId(id).collectList();
    }

    @PostMapping({ "/books/{id}/comments", "/{id}/comments/" })
    public Mono<CommentDto> addCommentForBook(@PathVariable("id") String id, @Valid @RequestBody CreateCommentDto dto) {
        return commentService.create(dto.getText(), id);
    }
}
