package ru.otus.hw.hw09.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw09.dto.BookDto;
import ru.otus.hw.hw09.dto.CreateBookDto;
import ru.otus.hw.hw09.dto.UpdateBookDto;
import ru.otus.hw.hw09.exceptions.EntityNotFoundException;
import ru.otus.hw.hw09.mappers.BookMapper;
import ru.otus.hw.hw09.models.Author;
import ru.otus.hw.hw09.models.Book;
import ru.otus.hw.hw09.models.Genre;
import ru.otus.hw.hw09.repositories.AuthorRepository;
import ru.otus.hw.hw09.repositories.BookRepository;
import ru.otus.hw.hw09.repositories.GenreRepository;
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
    public BookDto findById(long id) {
        var book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Книга с id = %d не найдена!".formatted(id));
        }
        return bookMapper.bookToDto(book.get());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookMapper::bookToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDto insert(CreateBookDto dto) {
        var author = findAuthorById(dto.getAuthorId());
        var genres = findGenresByIds(dto.getGenreIds());
        var newBook = new Book(0, dto.getTitle(), author, genres);
        var savedBook = bookRepository.save(newBook);
        return bookMapper.bookToDto(savedBook);
    }

    @Transactional
    @Override
    public BookDto update(UpdateBookDto dto) {
        var book = findBookById(dto.getId());
        var author = findAuthorById(dto.getAuthorId());
        var genres = findGenresByIds(dto.getGenreIds());

        book.setTitle(dto.getTitle());
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
        var genres = genreRepository.findAllById(ids);
        if (isEmpty(genres) || ids.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(ids));
        }
        return genres;
    }
}
