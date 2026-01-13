package ru.otus.hw.hw08.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw08.dto.BookDto;
import ru.otus.hw.hw08.exceptions.EntityNotFoundException;
import ru.otus.hw.hw08.mappers.BookMapper;
import ru.otus.hw.hw08.models.Author;
import ru.otus.hw.hw08.models.Book;
import ru.otus.hw.hw08.models.Genre;
import ru.otus.hw.hw08.repositories.AuthorRepository;
import ru.otus.hw.hw08.repositories.BookRepository;
import ru.otus.hw.hw08.repositories.CommentRepository;
import ru.otus.hw.hw08.repositories.GenreRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final CommentRepository commentRepository;

    @Override
    public Optional<BookDto> findById(String id) {
        var book = bookRepository.findById(id);
        return book.map(bookMapper::bookToDto);
    }

    @Override
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookMapper::bookToDto).collect(Collectors.toList());
    }

    @Override
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        var author = findAuthorById(authorId);
        var genres = findGenresByIds(genresIds);
        var newBook = new Book(null, title, author, genres);
        var savedBook = bookRepository.save(newBook);
        return bookMapper.bookToDto(savedBook);
    }

    @Override
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        var book = findBookById(id);
        var author = findAuthorById(authorId);
        var genres = findGenresByIds(genresIds);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);

        var savedBook = bookRepository.save(book);
        return bookMapper.bookToDto(savedBook);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Author findAuthorById(String id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Author with id %s not found".formatted(id)));
    }

    private Book findBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    private List<Genre> findGenresByIds(Set<String> ids) {
        var genres = genreRepository.findAllById(ids);
        if (isEmpty(genres) || ids.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(ids));
        }
        return genres;
    }
}
