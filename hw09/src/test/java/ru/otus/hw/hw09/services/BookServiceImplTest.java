package ru.otus.hw.hw09.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw09.dto.AuthorDto;
import ru.otus.hw.hw09.dto.BookDto;
import ru.otus.hw.hw09.dto.CreateBookDto;
import ru.otus.hw.hw09.dto.GenreDto;
import ru.otus.hw.hw09.dto.UpdateBookDto;
import ru.otus.hw.hw09.mappers.AuthorMapper;
import ru.otus.hw.hw09.mappers.BookMapper;
import ru.otus.hw.hw09.mappers.GenreMapper;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.hw09.TestDb.getDbAuthors;
import static ru.otus.hw.hw09.TestDb.getDbBooks;
import static ru.otus.hw.hw09.TestDb.getDbGenres;

@DataJpaTest
@Import({ BookServiceImpl.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

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
        assertThat(actualBookDto).isNotNull();

        var expectedBookDto = dbBooks.stream().filter(book -> book.getId() == bookId).findFirst().orElseThrow();

        assertThat(actualBookDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedBookDto);
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
    }

    @DisplayName("должен корректно создавать новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateNewBook() {
        var bookDto = bookService.insert(new CreateBookDto("test", 1L, Set.of(1L, 2L)));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto).matches(dto -> dto.getId() > 0);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isNotNull();
        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }

    @DisplayName("должен корректно обновлять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateBook() {
        var bookDto = bookService.update(new UpdateBookDto(1L, "test", 1L, Set.of(1L, 2L)));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto)
                .matches(dto -> dto.getTitle().equals("test"))
                .matches(dto -> dto.getAuthorDto().getId() == 1)
                .matches(dto -> dto.getGenreDtos().size() == 2);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isNotNull();
        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }
}
