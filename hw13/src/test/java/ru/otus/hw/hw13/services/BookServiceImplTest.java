package ru.otus.hw.hw13.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw13.dto.AuthorDto;
import ru.otus.hw.hw13.dto.BookDto;
import ru.otus.hw.hw13.dto.CreateBookDto;
import ru.otus.hw.hw13.dto.GenreDto;
import ru.otus.hw.hw13.dto.UpdateBookDto;
import ru.otus.hw.hw13.services.book.BookServiceImpl;
import ru.otus.hw.hw13.services.util.UserInitializerService;
import ru.otus.hw.hw13.utils.WithMockAppUser;

import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static ru.otus.hw.hw13.utils.TestDb.getDbAuthors;
import static ru.otus.hw.hw13.utils.TestDb.getDbBooks;
import static ru.otus.hw.hw13.utils.TestDb.getDbGenres;

@SpringBootTest
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

    @DisplayName("админ может корректно создавать новую книгу с произвольным автором")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "ADMIN")
    void adminShouldCreateNewBookWithArbitraryAuthor() {
        var bookDto = bookService.insert(new CreateBookDto("test", 1L, Set.of(1L, 2L)));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto).matches(dto -> dto.getId() > 0);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isNotNull();
        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }

    @DisplayName("обычному пользователю нельзя создавать новую книгу с произвольным автором")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER")
    void userShouldNotCreateNewBookWithArbitraryAuthor() {
        var dto = new CreateBookDto("test", 1L, Set.of(1L, 2L));
        assertThatThrownBy(() -> bookService.insert(dto))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("обычный пользователь может создавать новую книгу под своим авторством")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER")
    void userShouldCreateNewBookWithHisAuthority() {
        var bookDto = bookService.insert(new CreateBookDto("test", Set.of(1L, 2L)));
        assertThat(bookDto).isNotNull();
        assertThat(bookDto).matches(dto -> dto.getId() > 0);

        var foundBook = bookService.findById(bookDto.getId());
        assertThat(foundBook).isNotNull();
        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(bookDto);
    }

    @DisplayName("админ может корректно обновлять книгу и иметь возможность обновить автора")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "ADMIN")
    void adminShouldUpdateBookWithArbitraryAuthor() {
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

    @DisplayName("обычный пользователь не может обновлять книгу, если это не его книга")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER")
    void userShouldNotUpdateNotHisBook() {
        var dto = new UpdateBookDto(1L, "test", null, Set.of(1L, 2L));
        assertThatThrownBy(() -> bookService.update(dto))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("обычный пользователь не может обновлять книгу, если меняет автора, даже если это его книга")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER")
    void userShouldNotUpdateBookWithArbitraryAuthor() {
        // make sure that book is owned by that user
        var dto = new UpdateBookDto(1L, "test", 1L, Set.of(1L, 2L));
        assertThatThrownBy(() -> bookService.update(dto))
                .isInstanceOf(AccessDeniedException.class);
    }


}
