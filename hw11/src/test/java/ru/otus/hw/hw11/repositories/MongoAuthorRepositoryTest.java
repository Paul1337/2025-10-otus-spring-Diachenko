package ru.otus.hw.hw11.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.hw11.MongoDataInitializer;
import ru.otus.hw.hw11.models.Author;
import ru.otus.hw.hw11.repositories.AuthorRepository;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с авторами")
@DataMongoTest
@Import({ MongoDataInitializer.class })
public class MongoAuthorRepositoryTest {
    @Autowired
    private AuthorRepository repository;

    private static final int AUTHORS_COUNT = 3;

    @DisplayName("должен загружать список авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        StepVerifier.create(repository.findAll().collectList())
                .assertNext(authors -> {
                    assertThat(authors).hasSize(AUTHORS_COUNT);
                    assertThat(authors)
                            .allMatch(author -> author.getId() != null)
                            .allMatch(author -> author.getFullName() != null &&
                                    !author.getFullName().isBlank());
                })
                .verifyComplete();
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        Mono<Author> authorMono = repository.findAll().next();

        StepVerifier.create(authorMono.flatMap(a ->
                        repository.findById(a.getId()).map(found -> Map.entry(a, found))
                ))
                .assertNext(entry -> {
                    Author expected = entry.getKey();
                    Author actual = entry.getValue();

                    assertThat(actual)
                            .usingRecursiveComparison()
                            .isEqualTo(expected);
                })
                .verifyComplete();
    }
}
