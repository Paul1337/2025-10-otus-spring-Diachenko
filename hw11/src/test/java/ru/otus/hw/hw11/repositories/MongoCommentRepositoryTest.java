package ru.otus.hw.hw11.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.hw.hw11.MongoDataInitializer;
import ru.otus.hw.hw11.ObjectMapperConfig;
import ru.otus.hw.hw11.TestMongoDataInitializer;
import ru.otus.hw.hw11.models.Comment;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий на основе Mongo для работы с комментариями")
@DataMongoTest
@Import({ TestMongoDataInitializer.class })
class MongoCommentRepositoryTest {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private TestMongoDataInitializer initializer;

    @BeforeEach
    void setup() {
        initializer.init().block();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @ValueSource(strings = { "c1", "c2" })
    void shouldReturnCorrectCommentById(String id) {
        StepVerifier.create(repository.findById(id))
                .assertNext(comment -> {
                    assertThat(comment.getId()).isEqualTo(id);
                    assertThat(comment.getText()).isNotBlank();
                    assertThat(comment.getBookId()).isNotNull().isNotBlank();
                })
                .verifyComplete();
    }

    @DisplayName("должен загружать список комментариев для книги")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        StepVerifier.create(repository.findByBookId("b1").collectList())
                .assertNext(comments -> {
                    assertThat(comments).hasSize(2);
                    assertThat(comments).allMatch(c -> c.getText() != null && !c.getText().isBlank());
                    assertThat(comments).allMatch(c -> c.getBookId() != null && c.getBookId().equals("b1"));
                })
                .verifyComplete();
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewComment() {
        var newComment = new Comment(null, "TestComment", "b1");

        StepVerifier.create(repository.save(newComment))
                .assertNext(comment -> {
                    assertThat(comment.getId()).isNotNull();
                    assertThat(comment.getText()).isEqualTo("TestComment");
                    assertThat(comment.getBookId()).isEqualTo("b1");
                })
                .verifyComplete();
    }

    @DisplayName("должен сохранять обновлённый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedComment() {
        StepVerifier.create(repository.findById("c1")
                        .flatMap(comment -> {
                            comment.setText("Updated");
                            return repository.save(comment);
                        })
                )
                .assertNext(saved -> {
                    assertThat(saved.getText()).isEqualTo("Updated");
                })
                .verifyComplete();
    }
}