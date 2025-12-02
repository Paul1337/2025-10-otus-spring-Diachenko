package ru.otus.hw.hw06.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.converters.AuthorConverter;
import ru.otus.hw.hw06.converters.GenreConverter;
import ru.otus.hw.hw06.dto.AuthorDto;
import ru.otus.hw.hw06.dto.BookDto;
import ru.otus.hw.hw06.models.Author;
import ru.otus.hw.hw06.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto bookToDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorMapper.authorToDto(book.getAuthor()),
                book.getGenres().stream().map(genreMapper::genreToDto).collect(Collectors.toList())
        );
    }
}
