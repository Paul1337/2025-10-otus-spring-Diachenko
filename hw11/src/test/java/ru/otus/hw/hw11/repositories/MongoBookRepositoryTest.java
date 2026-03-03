package ru.otus.hw.hw11.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;
import ru.otus.hw.hw11.MongoDataInitializer;
import ru.otus.hw.hw11.models.Author;
import ru.otus.hw.hw11.models.Book;
import ru.otus.hw.hw11.models.Genre;
import ru.otus.hw.hw11.repositories.BookRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с книгами")
@DataMongoTest
@Import({ MongoDataInitializer.class })
class MongoBookRepositoryTest {
    @Autowired
    private BookRepository repository;

    private static final int BOOKS_COUNT = 3;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @ValueSource(strings = {"b1", "b2", "b3"})
    void shouldReturnCorrectBookById(String bookId) {
        StepVerifier.create(repository.findById(bookId))
                .assertNext(book -> {
                    assertThat(book.getId()).isEqualTo(bookId);
                    assertThat(book.getTitle()).isNotBlank();
                    assertThat(book.getAuthor()).isNotNull();
                    assertThat(book.getGenres()).isNotEmpty();
                })
                .verifyComplete();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(BOOKS_COUNT)
                .verifyComplete();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book newBook = new Book(
                null,
                "BookTitle_10500",
                new Author("a1", "Author_1"),
                Set.of(
                        new Genre("g1", "Genre_1"),
                        new Genre("g2", "Genre_2")
                )
        );

        StepVerifier.create(repository.save(newBook))
                .assertNext(saved -> {
                    assertThat(saved.getId()).isNotNull();
                    assertThat(saved.getTitle()).isEqualTo("BookTitle_10500");
                    assertThat(saved.getAuthor()).isNotNull();
                    assertThat(saved.getGenres()).hasSize(2);
                })
                .verifyComplete();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        StepVerifier.create(repository.findById("b1")
                        .flatMap(book -> {
                            book.setTitle("UpdatedTitle");
                            return repository.save(book);
                        })
                )
                .assertNext(updated -> {
                    assertThat(updated.getTitle()).isEqualTo("UpdatedTitle");
                })
                .verifyComplete();
    }

    @DisplayName("должен удалять книгу по id")
    @ParameterizedTest
    @ValueSource(strings = {"b1", "b2", "b3"})
    void shouldDeleteBook(String bookId) {
        StepVerifier.create(repository.deleteById(bookId))
                .verifyComplete();

        StepVerifier.create(repository.findById(bookId))
                .verifyComplete();
    }
}