package ru.otus.hw.hw08.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.hw08.config.MongoConfig;
import ru.otus.hw.hw08.config.MongoDataInitializer;
import ru.otus.hw.hw08.config.ObjectMapperConfig;
import ru.otus.hw.hw08.converters.CommentConverter;
import ru.otus.hw.hw08.dto.CommentDto;
import ru.otus.hw.hw08.mappers.CommentMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({ MongoDataInitializer.class, MongoConfig.class, ObjectMapperConfig.class, CommentServiceImpl.class, CommentConverter.class, CommentMapper.class})
public class CommentServiceImplTest {
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentConverter commentConverter;

    private List<ru.otus.hw.hw08.dto.CommentDto> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @DisplayName("должен загружать комметарий по id")
    @ParameterizedTest
    @ValueSource(strings = { "c1", "c2" })
    void shouldLoadCommentById(String commentId) {
        var actualCommentDto = commentService.findById(commentId);

        var expectedCommectDto = dbComments.stream().filter(dto -> dto.getId().equals(commentId)).findFirst().orElseThrow();
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
        var commentDtos = commentService.findAllByBookId("b1");
        assertThat(commentDtos).isNotNull().isNotEmpty();
        assertThat(commentDtos).hasSize(expectedCount);
        assertThat(commentDtos)
                .allMatch(dto -> dto.getId() != null)
                .allMatch(dto -> dto.getText() != null);

        var commentsStr = commentDtos.stream().map(commentConverter::commentDtoToString).collect(Collectors.toList());
        System.out.println(commentsStr);
    }

    @DisplayName("должен создавать новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateNewComment() {
        var commentDto = commentService.create("test", "b1");
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getText()).isEqualTo("test");
        assertThat(commentDto).matches(dto -> dto.getId() != null);

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
        var commentDto = commentService.update("c1", "updated");
        assertThat(commentDto).isNotNull();
        assertThat(commentDto)
                .matches(dto -> dto.getText().equals("updated"))
                .matches(dto -> dto.getId().equals("c1"));

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
        assertThat(commentService.findById("c1")).isPresent();
        commentService.deleteById("c1");
        assertThat(commentService.findById("c1")).isEmpty();
    }

    private static List<CommentDto> getDbComments() {
        return IntStream.range(1, 6).boxed()
                .map(id -> new CommentDto("c" + id,
                        "Comment_" + id
                ))
                .toList();
    }
}
