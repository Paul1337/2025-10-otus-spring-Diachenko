package ru.otus.hw.hw06.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.hw06.converters.CommentConverter;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
//@DataJpaTest
//@Import({ CommentServiceImpl.class, CommentConverter.class, JpaCommentRepository.class, JpaBookRepository.class})
//@Transactional(propagation = Propagation.NEVER)
public class CommentServiceTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentConverter commentConverter;

    @DisplayName("получение комментария по id: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    void shouldNotThrowWhenFindByIdAndConvertingDtoToString() {
        var commentDto = commentService.findById(1L);
        assertThat(commentDto).isPresent();

        assertThatNoException()
                .isThrownBy(() -> {
                    String dtoAsString = commentConverter.commentDtoToString(commentDto.get());
                    System.out.println(dtoAsString);
                });
    }

    @DisplayName("получение списка комментариев по id: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    void shouldNotThrowWhenFindByBookIdAndConvertingDtoToString() {
        var commentDtos = commentService.findAllByBookId(1L);
        assertThat(commentDtos).isNotNull().isNotEmpty();

        assertThatNoException()
                .isThrownBy(() -> {
                    var commentsStr = commentDtos.stream().map(commentConverter::commentDtoToString).collect(Collectors.toList());
                    System.out.println(commentsStr);
                });
    }

    @DisplayName("создание комментария: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldNotThrowWheInsertAndConvertingDtoToString() {
        var commentDto = commentService.insert("test", 1);
        assertThat(commentDto).isNotNull();

        assertThatNoException()
                .isThrownBy(() -> {
                    var commentStr = commentConverter.commentDtoToString(commentDto);
                    System.out.println(commentStr);
                });
    }

}
