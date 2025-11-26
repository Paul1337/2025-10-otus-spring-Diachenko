package ru.otus.hw.hw06.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.hw06.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий на основе jpa для работы с комментариями")
@DataJpaTest
@Import({ JpaCommentRepository.class })
public class JpaCommentRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private JpaCommentRepository repository;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @ValueSource(longs = { 1, 2 })
    void shouldReturnCorrectCommentById(long id) {
        var actualComment = repository.findById(id);
        em.clear();
        var expectedComment = em.find(Comment.class, id);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев для книги")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = repository.findByBookId(1L);

        int expectedCommentsSize = 2;

        assertThat(actualComments)
                .hasSize(expectedCommentsSize)
                .allMatch(comment -> comment.getId() > 0)
                .allMatch(comment -> comment.getText() != null &&
                        !comment.getText().isBlank());
    }



}
