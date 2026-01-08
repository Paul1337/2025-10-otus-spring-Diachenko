package ru.otus.hw.hw08.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw08.dto.BookDto;
import ru.otus.hw.hw08.models.Author;
import ru.otus.hw.hw08.models.Book;
import ru.otus.hw.hw08.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto bookToDto(Book book, Author author, List<Genre> genres) {
        return new BookDto(book.getId(), book.getTitle(),
                authorMapper.authorToDto(author),
                genres.stream().map(genreMapper::genreToDto).collect(Collectors.toList())
        );
    }
}
