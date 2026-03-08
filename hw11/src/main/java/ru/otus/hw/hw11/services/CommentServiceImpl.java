package ru.otus.hw.hw11.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.CommentDto;
import ru.otus.hw.hw11.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11.mappers.CommentMapper;
import ru.otus.hw.hw11.models.Comment;
import ru.otus.hw.hw11.repositories.BookRepository;
import ru.otus.hw.hw11.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public Flux<CommentDto> findAllByBookId(String bookId) {
        return bookRepository.existsById(bookId)
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.error(
                                new EntityNotFoundException("Book with id %s not found".formatted(bookId))
                        );
                    }

                    return commentRepository
                            .findByBookId(bookId)
                            .map(commentMapper::commentToDto);
                });
    }

    @Override
    public Mono<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Comment with id %s not found".formatted(id))
                ))
                .map(commentMapper::commentToDto);
    }

    @Override
    public Mono<CommentDto> create(String text, String bookId) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id %s not found".formatted(bookId))
                ))
                .flatMap(book -> {
                    Comment comment = new Comment();
                    comment.setText(text);
                    comment.setBookId(book.getId());
                    return commentRepository.save(comment);
                })
                .map(commentMapper::commentToDto);
    }

    @Override
    public Mono<CommentDto> update(String id, String text) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Comment with id %s not found".formatted(id))
                ))
                .flatMap(comment -> {
                    comment.setText(text);
                    return commentRepository.save(comment);
                })
                .map(commentMapper::commentToDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteById(id);
    }

}
