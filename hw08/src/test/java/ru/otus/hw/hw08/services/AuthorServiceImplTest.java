package ru.otus.hw.hw08.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.hw08.config.MongoDataInitializer;
import ru.otus.hw.hw08.config.ObjectMapperConfig;
import ru.otus.hw.hw08.converters.AuthorConverter;
import ru.otus.hw.hw08.converters.GenreConverter;
import ru.otus.hw.hw08.dto.AuthorDto;
import ru.otus.hw.hw08.dto.GenreDto;
import ru.otus.hw.hw08.mappers.AuthorMapper;
import ru.otus.hw.hw08.mappers.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({ MongoDataInitializer.class, ObjectMapperConfig.class, AuthorServiceImpl.class, AuthorConverter.class, AuthorMapper.class})
public class AuthorServiceImplTest {
    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private AuthorConverter authorConverter;

    private List<AuthorDto> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldLoadAllAuthors() {
        var authorDtos = authorService.findAll();
        assertThat(authorDtos).isNotNull().isNotEmpty();

        assertThat(authorDtos).hasSize(dbAuthors.size());
        assertThat(authorDtos)
                .allMatch(book -> book.getId() != null)
                .allMatch(book -> book.getFullName() != null);

        var dtosStr = authorDtos.stream().map(authorConverter::authorDtoToString).collect(Collectors.toList());
        System.out.println(dtosStr);
    }


    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto("a" + id, "Author_" + id))
                .toList();
    }

}
