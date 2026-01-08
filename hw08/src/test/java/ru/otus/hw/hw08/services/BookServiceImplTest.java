package ru.otus.hw.hw08.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.hw08.config.MongoDataInitializer;
import ru.otus.hw.hw08.config.ObjectMapperConfig;
import ru.otus.hw.hw08.converters.AuthorConverter;
import ru.otus.hw.hw08.converters.BookConverter;
import ru.otus.hw.hw08.converters.GenreConverter;
import ru.otus.hw.hw08.dto.AuthorDto;
import ru.otus.hw.hw08.dto.BookDto;
import ru.otus.hw.hw08.dto.GenreDto;
import ru.otus.hw.hw08.mappers.AuthorMapper;
import ru.otus.hw.hw08.mappers.BookMapper;
import ru.otus.hw.hw08.mappers.GenreMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({ MongoDataInitializer.class, ObjectMapperConfig.class, BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
public class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private BookMapper bookMapper;

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
    @ValueSource(strings = { "b1", "b2" })
    void shouldLoadBookById(String bookId) {
        var actualBookDto = bookService.findById(bookId);
        assertThat(actualBookDto).isPresent();

        var expectedBookDto = dbBooks.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElseThrow();

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
                .allMatch(book -> book.getId() != null)
                .allMatch(book -> book.getAuthorDto() != null)
                .allMatch(book -> book.getTitle() != null);

        var dtosStr = bookDtos.stream().map(bookConverter::bookDtoToString).collect(Collectors.toList());
        System.out.println(dtosStr);
    }

    @DisplayName("должен корректно создавать новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateNewBook() {
        var bookDto = bookService.insert("test", "a1", Set.of("g1", "g2"));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto).matches(dto -> dto.getId() != null);
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
        var bookDto = bookService.update("b1", "test", "a1", Set.of("g1", "g2"));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto)
                .matches(dto -> dto.getTitle().equals("test"))
                .matches(dto -> dto.getAuthorDto().getId().equals("a1"))
                .matches(dto -> dto.getGenreDtos().size() == 2);

        String dtoAsString = bookConverter.bookDtoToString(bookDto);
        System.out.println(dtoAsString);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get())
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto("a" + id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto("g" + id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto("b" + id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
