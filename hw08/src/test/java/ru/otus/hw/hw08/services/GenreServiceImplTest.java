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
import ru.otus.hw.hw08.config.MongoDataInitializer;
import ru.otus.hw.hw08.config.ObjectMapperConfig;
import ru.otus.hw.hw08.converters.CommentConverter;
import ru.otus.hw.hw08.converters.GenreConverter;
import ru.otus.hw.hw08.dto.CommentDto;
import ru.otus.hw.hw08.dto.GenreDto;
import ru.otus.hw.hw08.mappers.CommentMapper;
import ru.otus.hw.hw08.mappers.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({ MongoDataInitializer.class, ObjectMapperConfig.class, GenreServiceImpl.class, GenreConverter.class, GenreMapper.class})
public class GenreServiceImplTest {
    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private GenreConverter genreConverter;

    private List<GenreDto> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldLoadAllGenres() {
        var genreDtos = genreService.findAll();
        assertThat(genreDtos).isNotNull().isNotEmpty();

        assertThat(genreDtos).hasSize(dbGenres.size());
        assertThat(genreDtos)
                .allMatch(book -> book.getId() != null)
                .allMatch(book -> book.getName() != null);

        var dtosStr = genreDtos.stream().map(genreConverter::genreDtoToString).collect(Collectors.toList());
        System.out.println(dtosStr);
    }


    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto("g" + id, "Genre_" + id))
                .toList();
    }

}
