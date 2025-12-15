package ru.otus.hw.hw06.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw06.converters.AuthorConverter;
import ru.otus.hw.hw06.converters.BookConverter;
import ru.otus.hw.hw06.converters.GenreConverter;
import ru.otus.hw.hw06.mappers.AuthorMapper;
import ru.otus.hw.hw06.mappers.BookMapper;
import ru.otus.hw.hw06.mappers.GenreMapper;
import ru.otus.hw.hw06.repositories.BookRepository;
import ru.otus.hw.hw06.repositories.JpaAuthorRepository;
import ru.otus.hw.hw06.repositories.JpaBookRepository;
import ru.otus.hw.hw06.repositories.JpaGenreRepository;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@Import({ BookServiceImpl.class, BookConverter.class, JpaAuthorRepository.class, JpaGenreRepository.class, JpaBookRepository.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен корректно загружать книгу по id")
    @Test
    void shouldNotThrowWhenFindByIdAndConvertingDtoToString() {
        var bookDto = bookService.findById(1L);
        assertThat(bookDto).isPresent();

        var expectedBook = bookRepository.findById(1L);
        var expectedBookDto = bookMapper.bookToDto(expectedBook.orElseThrow());

        assertThat(bookDto.get())
                .usingRecursiveComparison()
                .isEqualTo(expectedBookDto);

        var dtoAsString = bookConverter.bookDtoToString(bookDto.get());
        System.out.println(dtoAsString);
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
