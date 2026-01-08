package ru.otus.hw.hw08.services;

import ru.otus.hw.hw08.dto.CommentDto;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllByBookId(String bookId);

    Optional<CommentDto> findById(String id);

    CommentDto create(String text, String bookId);

    CommentDto update(String id, String text);

    void deleteById(String id);
}
