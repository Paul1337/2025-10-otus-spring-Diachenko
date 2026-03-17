package ru.otus.hw.hw13.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw13.dto.CommentDto;
import ru.otus.hw.hw13.mappers.CommentMapper;
import ru.otus.hw.hw13.services.comment.CommentServiceImpl;
import ru.otus.hw.hw13.utils.WithMockAppUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.otus.hw.hw13.utils.TestDb.getDbComments;

@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceImplTest {
    @Autowired
    private CommentServiceImpl commentService;

    private List<CommentDto> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @DisplayName("должен загружать комметарий по id")
    @ParameterizedTest
    @ValueSource(longs = { 1L, 2L })
    void shouldLoadCommentById(long commentId) {
        var actualCommentDto = commentService.findById(commentId);

        var expectedCommectDto = dbComments.stream().filter(dto -> dto.getId() == commentId).findFirst().orElseThrow();
        assertThat(actualCommentDto).isPresent();
        assertThat(actualCommentDto.get())
                .usingRecursiveComparison()
                .isEqualTo(expectedCommectDto);
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldLoadCommentsByIdList() {
        final int expectedCount = 2;
        var commentDtos = commentService.findAllByBookId(1L);
        assertThat(commentDtos).isNotNull().isNotEmpty();
        assertThat(commentDtos).hasSize(expectedCount);
        assertThat(commentDtos)
                .allMatch(dto -> dto.getId() > 0)
                .allMatch(dto -> dto.getText() != null);
    }

    @DisplayName("любоый пользователь может создавать новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER")
    void shouldCreateNewComment() {
        var commentDto = commentService.create("test", 1);
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getText()).isEqualTo("test");
        assertThat(commentDto).matches(dto -> dto.getId() > 0);

        var foundComment = commentService.findById(commentDto.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get())
                .usingRecursiveComparison()
                .isEqualTo(commentDto);
    }

    @DisplayName("админ может обновлять любой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "ADMIN", id = 2)
    void adminShouldUpdateComment() {
        var commentDto = commentService.update(1, "updated");
        assertThat(commentDto).isNotNull();
        assertThat(commentDto)
                .matches(dto -> dto.getText().equals("updated"))
                .matches(dto -> dto.getId() == 1);

        var foundComment = commentService.findById(commentDto.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get())
                .usingRecursiveComparison()
                .isEqualTo(commentDto);
    }

    @DisplayName("обычный пользователь не может обновлять чужой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER", id = 2)
    void userShouldUpdateNotHisComment() {
        assertThatThrownBy(
                () -> commentService.update(1, "updated")
        ).isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("обычный пользователь может обновлять свой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER", id = 1)
    void userShouldUpdateHisComment() {
        var commentDto = commentService.update(1, "updated");
        assertThat(commentDto).isNotNull();
        assertThat(commentDto)
                .matches(dto -> dto.getText().equals("updated"))
                .matches(dto -> dto.getId() == 1);

        var foundComment = commentService.findById(commentDto.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get())
                .usingRecursiveComparison()
                .isEqualTo(commentDto);
    }

    @DisplayName("админ может удалять любой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "ADMIN", id = 2)
    void adminShouldRemoveComment() {
        assertThat(commentService.findById(1L)).isPresent();
        commentService.deleteById(1L);
        assertThat(commentService.findById(1L)).isEmpty();
    }

    @DisplayName("модератор может удалять любой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "MODERATOR", id = 2)
    void moderatorShouldRemoveComment() {
        assertThat(commentService.findById(1L)).isPresent();
        commentService.deleteById(1L);
        assertThat(commentService.findById(1L)).isEmpty();
    }

    @DisplayName("обычный пользователь не может удалять любой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER", id = 2)
    void userShouldNotRemoveNotHisComment() {
        assertThatThrownBy(
                () -> commentService.deleteById(1L)
        ).isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("обычный пользователь может удалять свой комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockAppUser(role = "USER", id = 1)
    void userShouldRemoveHisComment() {
        assertThat(commentService.findById(1L)).isPresent();
        commentService.deleteById(1L);
        assertThat(commentService.findById(1L)).isEmpty();
    }
}
