package ru.otus.hw.hw06.repositories;

import ru.otus.hw.hw06.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findByBookId(long bookId);

    Comment save(Comment comment);
}
