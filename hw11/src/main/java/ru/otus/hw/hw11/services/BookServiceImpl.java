package ru.otus.hw.hw11.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.dto.AuthorDto;
import ru.otus.hw.hw11.dto.BookDto;
import ru.otus.hw.hw11.dto.CreateBookDto;
import ru.otus.hw.hw11.dto.GenreDto;
import ru.otus.hw.hw11.dto.UpdateBookDto;
import ru.otus.hw.hw11.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11.mappers.BookMapper;
import ru.otus.hw.hw11.mappers.GenreMapper;
import ru.otus.hw.hw11.models.Author;
import ru.otus.hw.hw11.models.Book;
import ru.otus.hw.hw11.models.Genre;
import ru.otus.hw.hw11.repositories.AuthorRepository;
import ru.otus.hw.hw11.repositories.BookRepository;
import ru.otus.hw.hw11.repositories.GenreRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
//    private final AuthorService authorService;

//    private final GenreService genreService;

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    private final GenreMapper genreMapper;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id %s not found".formatted(id))
                ))
                .map(bookMapper::bookToDto);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(bookMapper::bookToDto);
    }

    @Override
    public Mono<BookDto> insert(CreateBookDto dto) {
        var authorMono = authorRepository.findById(dto.getAuthorId());
        var genresMono = findGenresByIds(dto.getGenreIds());

        return Mono.zip(authorMono, genresMono)
                .flatMap(tuple -> {
                    Author author = tuple.getT1();
                    Set<Genre> genres = tuple.getT2();

                    Book book = new Book(
                            null,
                            dto.getTitle(),
                            author,
                            genres
                    );

                    return bookRepository.save(book);
                })
                .map(bookMapper::bookToDto);
    }

    @Override
    public Mono<BookDto> update(UpdateBookDto dto) {
        var bookMono = bookRepository.findById(dto.getId());
        var authorMono = authorRepository.findById(dto.getAuthorId());
        var genresMono = findGenresByIds(dto.getGenreIds());

        return Mono.zip(bookMono, authorMono, genresMono)
                        .flatMap(tuple -> {
                            var book = tuple.getT1();
                            book.setTitle(dto.getTitle());
                            book.setAuthor(tuple.getT2());
                            book.setGenres(tuple.getT3());

                            return bookRepository.save(book);
                        })
                .map(bookMapper::bookToDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Set<Genre>> findGenresByIds(Set<String> ids) {
        return genreRepository.findAllById(ids)
                .collect(Collectors.toSet())
                .flatMap(genres -> {
                    if (genres.isEmpty() || genres.size() != ids.size()) {
                        return Mono.error(new EntityNotFoundException(
                                "One or all genres with ids %s not found".formatted(ids)
                        ));
                    }
                    return Mono.just(genres);
                });
    }


}
