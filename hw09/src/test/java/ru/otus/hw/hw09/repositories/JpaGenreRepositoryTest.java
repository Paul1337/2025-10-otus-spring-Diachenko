package ru.otus.hw.hw09.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.hw09.models.Genre;
import ru.otus.hw.hw09.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
class JpaGenreRepositoryTest {
    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    private static final int GENRES_COUNT = 6;

    @DisplayName("должен загружать жанры по списку id")
    @Test
    void shouldReturnCorrectGenreListByIdsList() {
        var expectedGenres = List.of(
                em.find(Genre.class, 1L),
                em.find(Genre.class, 2L),
                em.find(Genre.class, 3L)
        );
        em.clear();
        var actualGenres = repository.findAllById(
                expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet())
        );

        assertThat(actualGenres).hasSize(expectedGenres.size());
        assertThat(actualGenres)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repository.findAll();

        assertThat(actualGenres).hasSize(GENRES_COUNT);
        assertThat(actualGenres)
                .allMatch(genre -> genre.getId() > 0)
                .allMatch(genre -> genre.getName() != null && !genre.getName().isBlank());
    }
}