package ru.otus.hw.hw10.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.hw10.models.Author;
import ru.otus.hw.hw10.repositories.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
public class JpaAuthorRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository repository;

    private static final int AUTHORS_COUNT = 3;

    @DisplayName("должен загружать список авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repository.findAll();

        assertThat(actualAuthors).hasSize(AUTHORS_COUNT);
        assertThat(actualAuthors)
                .allMatch(author -> author.getId() > 0)
                .allMatch(author -> author.getFullName() != null &&
                                    !author.getFullName().isBlank());
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @ValueSource(longs = { 1, 2 })
    void shouldReturnCorrectAuthorById(long id) {
        var actualAuthor = repository.findById(id);
        em.clear();
        var expectedAuthor = em.find(Author.class, id);

        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }
}
