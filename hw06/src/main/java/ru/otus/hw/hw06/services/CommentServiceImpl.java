package ru.otus.hw.hw06.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw06.converters.CommentConverter;
import ru.otus.hw.hw06.dto.CommentDto;
import ru.otus.hw.hw06.exceptions.EntityNotFoundException;
import ru.otus.hw.hw06.models.Comment;
import ru.otus.hw.hw06.repositories.BookRepository;
import ru.otus.hw.hw06.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    private final BookRepository bookRepository;

    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        var comments = commentRepository.findByBookId(bookId);
        return comments.stream().map(commentConverter::commentToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> findById(long id) {
        var comment = commentRepository.findById(id);
        return comment.map(commentConverter::commentToDto);
    }

    @Override
    @Transactional
    public CommentDto insert(String text, long bookId) {
        var comment = save(0, text, bookId);
        return commentConverter.commentToDto(comment);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }
}
