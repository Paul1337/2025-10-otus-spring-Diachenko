package ru.otus.hw.hw11.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11.models.Comment;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByBookId(String bookId);
}
