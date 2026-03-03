package ru.otus.hw.hw11.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.CommentDto;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    Flux<CommentDto> findAllByBookId(String bookId);

    Mono<CommentDto> findById(String id);

    Mono<CommentDto>  create(String text, String bookId);

    Mono<CommentDto>  update(String id, String text);

    Mono<Void> deleteById(String id);
}
