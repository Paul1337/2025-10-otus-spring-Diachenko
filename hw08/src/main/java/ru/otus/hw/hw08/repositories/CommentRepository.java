package ru.otus.hw.hw08.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.hw08.models.Comment;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBookId(String bookId);

    void deleteByBookId(String bookId);
}
