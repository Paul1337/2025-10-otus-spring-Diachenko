package ru.otus.hw.hw06.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw06.converters.CommentConverter;
import ru.otus.hw.hw06.dto.AuthorDto;
import ru.otus.hw.hw06.dto.BookDto;
import ru.otus.hw.hw06.dto.CommentDto;
import ru.otus.hw.hw06.dto.GenreDto;
import ru.otus.hw.hw06.mappers.CommentMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@Import({ CommentServiceImpl.class, CommentConverter.class, CommentMapper.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceImplTest {
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentConverter commentConverter;

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

        String dtoAsString = commentConverter.commentDtoToString(actualCommentDto.get());
        System.out.println(dtoAsString);
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

        var commentsStr = commentDtos.stream().map(commentConverter::commentDtoToString).collect(Collectors.toList());
        System.out.println(commentsStr);
    }

    @DisplayName("должен создавать новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateNewComment() {
        var commentDto = commentService.create("test", 1);
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getText()).isEqualTo("test");
        assertThat(commentDto).matches(dto -> dto.getId() > 0);

        var commentStr = commentConverter.commentDtoToString(commentDto);
        System.out.println(commentStr);

        var foundComment = commentService.findById(commentDto.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get())
                .usingRecursiveComparison()
                .isEqualTo(commentDto);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateComment() {
        var commentDto = commentService.update(1, "updated");
        assertThat(commentDto).isNotNull();
        assertThat(commentDto)
                .matches(dto -> dto.getText().equals("updated"))
                .matches(dto -> dto.getId() == 1);

        var commentStr = commentConverter.commentDtoToString(commentDto);
        System.out.println(commentStr);

        var foundComment = commentService.findById(commentDto.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get())
                .usingRecursiveComparison()
                .isEqualTo(commentDto);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldRemoveComment() {
        assertThat(commentService.findById(1L)).isPresent();
        commentService.deleteById(1L);
        assertThat(commentService.findById(1L)).isEmpty();
    }


    private static List<CommentDto> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new CommentDto(id,
                        "Comment_" + id
                ))
                .toList();
    }
}
