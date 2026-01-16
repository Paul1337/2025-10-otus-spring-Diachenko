package ru.otus.hw.hw08.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw08.dto.CommentDto;
import ru.otus.hw.hw08.exceptions.EntityNotFoundException;
import ru.otus.hw.hw08.mappers.CommentMapper;
import ru.otus.hw.hw08.models.Comment;
import ru.otus.hw.hw08.repositories.BookRepository;
import ru.otus.hw.hw08.repositories.CommentRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        var comments = commentRepository.findByBookId(bookId);
        return comments.stream().map(commentMapper::commentToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> findById(String id) {
        var comment = commentRepository.findById(id);
        return comment.map(commentMapper::commentToDto);
    }

    @Override
    public CommentDto create(String text, String bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(null, text, book);
        var savedComment = commentRepository.save(comment);
        return commentMapper.commentToDto(savedComment);
    }

    @Override
    public CommentDto update(String id, String text) {
        var comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        comment.setText(text);
        commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

}
