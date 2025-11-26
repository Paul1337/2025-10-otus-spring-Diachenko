package ru.otus.hw.hw06.services;

import ru.otus.hw.hw06.dto.CommentDto;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllByBookId(long bookId);

    Optional<CommentDto> findById(long id);

    CommentDto insert(String text, long bookId);
}
