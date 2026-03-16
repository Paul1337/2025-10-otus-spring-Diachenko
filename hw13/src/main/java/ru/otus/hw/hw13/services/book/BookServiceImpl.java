package ru.otus.hw.hw13.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw13.dto.BookDto;
import ru.otus.hw.hw13.dto.CreateBookDto;
import ru.otus.hw.hw13.dto.UpdateBookDto;
import ru.otus.hw.hw13.exceptions.EntityNotFoundException;
import ru.otus.hw.hw13.mappers.BookMapper;
import ru.otus.hw.hw13.models.Author;
import ru.otus.hw.hw13.models.Book;
import ru.otus.hw.hw13.models.Genre;
import ru.otus.hw.hw13.models.User;
import ru.otus.hw.hw13.repositories.AuthorRepository;
import ru.otus.hw.hw13.repositories.BookRepository;
import ru.otus.hw.hw13.repositories.GenreRepository;
import ru.otus.hw.hw13.services.security.SecurityUtils;

import java.util.List;
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

    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Книга с id = %d не найдена!".formatted(id))
        );
        return bookMapper.bookToDto(book);
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
        var currentUser = securityUtils.findCurrentUser();

        if (dto.getAuthorId() != null && !securityUtils.hasRole("ADMIN")) {
            throw new AccessDeniedException("Only admin can set arbitrary author");
        }

        var author = dto.getAuthorId() != null
                ? findAuthorById(dto.getAuthorId())
                : getAuthorForUser(currentUser);

        var genres = findGenresByIds(dto.getGenreIds());
        var newBook = new Book(0, dto.getTitle(), author, genres);
        var savedBook = bookRepository.save(newBook);
        return bookMapper.bookToDto(savedBook);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN') or @bookSecurityService.isBookAuthor(#dto.id, authentication.principal.id)")
    public BookDto update(UpdateBookDto dto) {
        var currentUser = securityUtils.findCurrentUser();

        if (dto.getAuthorId() != null && !securityUtils.hasRole("ADMIN")) {
            throw new AccessDeniedException("Only admin can update author");
        }

        var author = dto.getAuthorId() != null
                ? findAuthorById(dto.getAuthorId())
                : getAuthorForUser(currentUser);

        var genres = findGenresByIds(dto.getGenreIds());

        var book = findBookById(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(author);
        book.setGenres(genres);

        var savedBook = bookRepository.save(book);
        return bookMapper.bookToDto(savedBook);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @bookSecurityService.isBookAuthor(#id, authentication.principal.id)")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthorForUser(User user) {
        var authorOptional = authorRepository.findByUserId(user.getId());
        if (authorOptional.isEmpty()) {
            var author = new Author(user.getFullName(), user);
            authorRepository.save(author);
            return author;
        } else {
            return authorOptional.get();
        }
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
