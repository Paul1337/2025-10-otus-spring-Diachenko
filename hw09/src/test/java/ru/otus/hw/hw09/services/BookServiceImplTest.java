package ru.otus.hw.hw09.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw09.converters.AuthorConverter;
import ru.otus.hw.hw09.converters.BookConverter;
import ru.otus.hw.hw09.converters.GenreConverter;
import ru.otus.hw.hw09.dto.AuthorDto;
import ru.otus.hw.hw09.dto.BookDto;
import ru.otus.hw.hw09.dto.GenreDto;
import ru.otus.hw.hw09.mappers.AuthorMapper;
import ru.otus.hw.hw09.mappers.BookMapper;
import ru.otus.hw.hw09.mappers.GenreMapper;
import ru.otus.hw.hw09.repositories.BookRepository;
import ru.otus.hw.hw09.services.BookServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.hw09.TestDb.getDbAuthors;
import static ru.otus.hw.hw09.TestDb.getDbBooks;
import static ru.otus.hw.hw09.TestDb.getDbGenres;

@DataJpaTest
@Import({ BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен корректно загружать книгу по id")
    @ParameterizedTest
    @ValueSource(longs = { 1L, 2L })
    void shouldLoadBookById(long bookId) {
        var actualBookDto = bookService.findById(bookId);
        assertThat(actualBookDto).isPresent();

        var expectedBookDto = dbBooks.stream().filter(book -> book.getId() == bookId).findFirst().orElseThrow();

        assertThat(actualBookDto.get())
                .usingRecursiveComparison()
                .isEqualTo(expectedBookDto);

        var dtoAsString = bookConverter.bookDtoToString(actualBookDto.get());
        System.out.println(dtoAsString);
    }

    @DisplayName("должен корректно загружать все книги")
    @Test
    void shouldLoadAllBooks() {
        var bookDtos = bookService.findAll();
        assertThat(bookDtos).isNotNull().isNotEmpty();

        assertThat(bookDtos).hasSize(dbBooks.size());
        assertThat(bookDtos)
                .allMatch(book -> book.getId() > 0)
                .allMatch(book -> book.getAuthorDto() != null)
                .allMatch(book -> book.getTitle() != null);

        var dtosStr = bookDtos.stream().map(bookConverter::bookDtoToString).collect(Collectors.toList());
        System.out.println(dtosStr);
    }

    @DisplayName("должен корректно создавать новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateNewBook() {
        var bookDto = bookService.insert("test", 1, Set.of(1L, 2L));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto).matches(dto -> dto.getId() > 0);
        String dtoAsString = bookConverter.bookDtoToString(bookDto);
        System.out.println(dtoAsString);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get())
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }

    @DisplayName("должен корректно обновлять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateBook() {
        var bookDto = bookService.update(1, "test", 1, Set.of(1L, 2L));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto)
                .matches(dto -> dto.getTitle().equals("test"))
                .matches(dto -> dto.getAuthorDto().getId() == 1)
                .matches(dto -> dto.getGenreDtos().size() == 2);

        String dtoAsString = bookConverter.bookDtoToString(bookDto);
        System.out.println(dtoAsString);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get())
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }
}
