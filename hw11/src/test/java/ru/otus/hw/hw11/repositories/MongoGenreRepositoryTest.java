package ru.otus.hw.hw11.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;
import ru.otus.hw.hw11.MongoDataInitializer;
import ru.otus.hw.hw11.ObjectMapperConfig;
import ru.otus.hw.hw11.TestMongoDataInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Репозиторий на основе Mongo для работы с жанрами")
@DataMongoTest
@Import({ TestMongoDataInitializer.class })
class MongoGenreRepositoryTest {
    @Autowired
    private GenreRepository repository;

    private static final int GENRES_COUNT = 6;

    @Autowired
    private TestMongoDataInitializer initializer;

    @BeforeEach
    void setup() {
        initializer.init().block();
    }

    @DisplayName("должен загружать жанры по списку id")
    @Test
    void shouldReturnCorrectGenreListByIdsList() {
        var ids = List.of("g1", "g2", "g3");

        StepVerifier.create(repository.findAllById(ids))
                .recordWith(ArrayList::new)
                .expectNextCount(ids.size())
                .expectRecordedMatches(genres ->
                        genres.stream().allMatch(g -> ids.contains(g.getId()))
                )
                .verifyComplete();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        StepVerifier.create(repository.findAll())
                .recordWith(ArrayList::new)
                .expectNextCount(GENRES_COUNT)
                .expectRecordedMatches(genres ->
                        genres.stream().allMatch(g -> g.getId() != null && !g.getId().isBlank()) &&
                                genres.stream().allMatch(g -> g.getName() != null && !g.getName().isBlank())
                )
                .verifyComplete();
    }
}