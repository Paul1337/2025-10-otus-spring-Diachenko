package ru.otus.hw.hw13.services.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw13.dto.CommentDto;
import ru.otus.hw.hw13.exceptions.EntityNotFoundException;
import ru.otus.hw.hw13.mappers.CommentMapper;
import ru.otus.hw.hw13.models.Comment;
import ru.otus.hw.hw13.repositories.BookRepository;
import ru.otus.hw.hw13.repositories.CommentRepository;
import ru.otus.hw.hw13.repositories.UserRepository;
import ru.otus.hw.hw13.services.security.SecurityUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    private final SecurityUtils securityUtils;

    private final UserRepository userRepository;

    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
        }
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

        var owner = securityUtils.findCurrentUser();

        var comment = new Comment(0, text, book, owner);
        var savedComment = commentRepository.save(comment);
        return commentMapper.commentToDto(savedComment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @commentSecurityService.isCommentAuthor(#id, authentication.principal.id)")
    public CommentDto update(long id, String text) {
        var comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setText(text);
        commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or @commentSecurityService.isCommentAuthor(#id, authentication.principal.id)")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
