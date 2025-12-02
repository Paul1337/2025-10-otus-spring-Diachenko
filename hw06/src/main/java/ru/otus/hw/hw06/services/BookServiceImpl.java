package ru.otus.hw.hw06.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw06.dto.BookDto;
import ru.otus.hw.hw06.exceptions.EntityNotFoundException;
import ru.otus.hw.hw06.mappers.BookMapper;
import ru.otus.hw.hw06.models.Author;
import ru.otus.hw.hw06.models.Book;
import ru.otus.hw.hw06.models.Genre;
import ru.otus.hw.hw06.repositories.AuthorRepository;
import ru.otus.hw.hw06.repositories.BookRepository;
import ru.otus.hw.hw06.repositories.GenreRepository;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        var book = bookRepository.findById(id);
        return book.map(bookMapper::bookToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookMapper::bookToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        var author = findAuthorById(authorId);
        var genres = findGenresByIds(genresIds);
        var newBook = new Book(0, title, author, genres);
        var savedBook = bookRepository.save(newBook);
        return bookMapper.bookToDto(savedBook);
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        var book = findBookById(id);
        var author = findAuthorById(authorId);
        var genres = findGenresByIds(genresIds);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);

        var savedBook = bookRepository.save(book);
        return bookMapper.bookToDto(savedBook);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author findAuthorById(long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    private Book findBookById(long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    private List<Genre> findGenresByIds(Set<Long> ids) {
        var genres = genreRepository.findAllByIds(ids);
        if (isEmpty(genres) || ids.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(ids));
        }
        return genres;
    }
}
