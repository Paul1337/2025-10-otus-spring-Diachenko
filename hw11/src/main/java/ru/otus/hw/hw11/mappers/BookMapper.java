package ru.otus.hw.hw11.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw11.dto.BookDto;
import ru.otus.hw.hw11.dto.GenreDto;
import ru.otus.hw.hw11.models.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto bookToDto(Book book) {
        List<GenreDto> genres = book.getGenres() == null
                ? List.of()
                : book.getGenres()
                .stream()
                .map(genreMapper::genreToDto)
                .toList();

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor() == null
                        ? null
                        : authorMapper.authorToDto(book.getAuthor()),
                genres
        );
    }
}
