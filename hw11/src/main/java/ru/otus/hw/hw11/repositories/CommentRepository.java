package ru.otus.hw.hw11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw11.models.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);
}
