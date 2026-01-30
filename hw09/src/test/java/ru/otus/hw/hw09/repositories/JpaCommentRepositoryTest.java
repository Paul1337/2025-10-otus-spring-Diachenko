package ru.otus.hw.hw09.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.hw09.models.Book;
import ru.otus.hw.hw09.models.Comment;
import ru.otus.hw.hw09.repositories.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий на основе jpa для работы с комментариями")
@DataJpaTest
public class JpaCommentRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository repository;

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

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, 1L);
        assertThat(book).isNotNull();
        var expectedComment = new Comment(0, "TestComment", book);
        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);

        em.flush();
        em.clear();

        var actualComment = repository.findById(returnedComment.getId());

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.get().getText()).isEqualTo(expectedComment.getText());
    }

    @DisplayName("должен сохранять обновлённый комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = em.find(Book.class, 1L);
        assertThat(book).isNotNull();
        var expectedComment = new Comment(1, "TestComment", book);
        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);

        em.flush();
        em.clear();

        var actualComment = repository.findById(returnedComment.getId());

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.get().getText()).isEqualTo(expectedComment.getText());
    }

}
