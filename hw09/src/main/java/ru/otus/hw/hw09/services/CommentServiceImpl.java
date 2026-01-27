package ru.otus.hw.hw09.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw09.dto.CommentDto;
import ru.otus.hw.hw09.exceptions.EntityNotFoundException;
import ru.otus.hw.hw09.mappers.CommentMapper;
import ru.otus.hw.hw09.models.Comment;
import ru.otus.hw.hw09.repositories.BookRepository;
import ru.otus.hw.hw09.repositories.CommentRepository;
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
    public List<CommentDto> findAllByBookId(long bookId) {
        var comments = commentRepository.findByBookId(bookId);
        return comments.stream().map(commentMapper::commentToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> findById(long id) {
        var comment = commentRepository.findById(id);
        return comment.map(commentMapper::commentToDto);
    }

    @Override
    @Transactional
    public CommentDto create(String text, long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(0, text, book);
        var savedComment = commentRepository.save(comment);
        return commentMapper.commentToDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text) {
        var comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setText(text);
        commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
