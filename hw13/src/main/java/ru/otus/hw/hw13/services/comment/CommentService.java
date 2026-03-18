package ru.otus.hw.hw13.services.comment;

import ru.otus.hw.hw13.dto.CommentDto;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllByBookId(long bookId);

    Optional<CommentDto> findById(long id);

    CommentDto create(String text, long bookId);

    CommentDto update(long id, String text);

    void deleteById(long id);
}
