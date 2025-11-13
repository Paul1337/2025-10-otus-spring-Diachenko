package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {
    @Autowired
    private JdbcGenreRepository repositoryJdbc;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанры по списку id")
    @ParameterizedTest
    @MethodSource("getDbGenresParts")
    void shouldReturnCorrectGenreListByIdsList(List<Genre> expectedGenres) {
        System.out.println(expectedGenres);
        var actualGenres = repositoryJdbc.findAllByIds(
                expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet())
        );
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJdbc.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<List<Genre>> getDbGenresParts() {
        var dbGenres = getDbGenres();

        return List.of(
                List.of(dbGenres.get(0), dbGenres.get(1)),
                List.of(dbGenres.get(1), dbGenres.get(2), dbGenres.get(3)),
                List.of(dbGenres.get(1), dbGenres.get(4), dbGenres.get(5)),
                List.of(dbGenres.get(5)),
                List.of()
            );
    }
}