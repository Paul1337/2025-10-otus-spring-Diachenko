package ru.otus.hw.hw06.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.hw06.models.Author;
import ru.otus.hw.hw06.models.Book;
import ru.otus.hw.hw06.models.Genre;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@Import({ JpaBookRepository.class })
public class JpaBookRepositoryTest {
    @Autowired
    private JpaBookRepository repository;

    @Autowired
    private TestEntityManager em;

    private static final int BOOKS_COUNT = 3;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void shouldReturnCorrectBookById(long bookId) {
        var actualBook = repository.findById(bookId);
        assertThat(actualBook).isPresent();

        em.clear();
        var expectedBook = em.find(Book.class, bookId);

        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("genres")
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        assertThat(actualBooks).hasSize(BOOKS_COUNT);
        assertThat(actualBooks)
                .allMatch(book -> book.getId() > 0)
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getTitle() != null)
                .allMatch(book -> book.getGenres() != null && !book.getGenres().isEmpty());
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = em.find(Author.class, 1);
        var genres = List.of(
                em.find(Genre.class, 1),
                em.find(Genre.class, 2)
        );

        var expectedBook = new Book(0, "BookTitle_10500", author, genres);
        var returnedBook = repository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        em.flush();
        em.clear();

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author = em.find(Author.class, 2);
        var genres = List.of(
                em.find(Genre.class, 4),
                em.find(Genre.class, 5)
        );
        var expectedBook = new Book(1L, "BookTitle_10500", author, genres);

        assertThat(repository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        em.flush();
        em.clear();

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @ParameterizedTest
    @ValueSource(longs = { 1, 2, 3 })
    void shouldDeleteBook(long bookId) {
        assertThat(repository.findById(bookId)).isPresent();
        repository.deleteById(bookId);
        assertThat(repository.findById(bookId)).isEmpty();
    }

}
