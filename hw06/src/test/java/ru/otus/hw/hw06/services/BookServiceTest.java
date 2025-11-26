package ru.otus.hw.hw06.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.hw06.converters.BookConverter;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
//@DataJpaTest
//@Import({ CommentServiceImpl.class, CommentConverter.class, JpaCommentRepository.class, JpaBookRepository.class})
//@Transactional(propagation = Propagation.NEVER)
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookConverter bookConverter;

    @DisplayName("получение книги по id: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    void shouldNotThrowWhenFindByIdAndConvertingDtoToString() {
        var bookDto = bookService.findById(1L);
        assertThat(bookDto).isPresent();

        assertThatNoException()
                .isThrownBy(() -> {
                    String dtoAsString = bookConverter.bookDtoToString(bookDto.get());
                    System.out.println(dtoAsString);
                });
    }

    @DisplayName("получение списка книг: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    void shouldNotThrowWhenFindAllAndConvertingDtoToString() {
        var bookDtos = bookService.findAll();
        assertThat(bookDtos).isNotNull().isNotEmpty();

        assertThatNoException()
                .isThrownBy(() -> {
                    var dtosStr = bookDtos.stream().map(bookConverter::bookDtoToString).collect(Collectors.toList());
                    System.out.println(dtosStr);
                });
    }

    @DisplayName("создание книги: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldNotThrowWhenInsertAndConvertingDtoToString() {
        var bookDto = bookService.insert("test", 1, Set.of(1L, 2L));
        assertThatNoException()
                .isThrownBy(() -> {
                    String dtoAsString = bookConverter.bookDtoToString(bookDto);
                    System.out.println(dtoAsString);
                });
    }

    @DisplayName("обновление книги: не должен выбросить LazyInitializationException при конвертации dto в строку")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldNotThrowWhenUpdateAndConvertingDtoToString() {
        var bookDto = bookService.update(1, "test", 1, Set.of(1L, 2L));
        assertThatNoException()
                .isThrownBy(() -> {
                    String dtoAsString = bookConverter.bookDtoToString(bookDto);
                    System.out.println(dtoAsString);
                });
    }

}
