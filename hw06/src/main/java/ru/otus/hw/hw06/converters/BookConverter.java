package ru.otus.hw.hw06.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.BookDto;
import ru.otus.hw.hw06.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookDtoToString(BookDto bookDto) {
        var genresString = bookDto.getGenreDtos().stream()
                .map(genreConverter::genreDtoToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));

        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                bookDto.getId(),
                bookDto.getTitle(),
                authorConverter.authorDtoToString(bookDto.getAuthorDto()),
                genresString);
    }

    public BookDto bookToDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.authorToDto(book.getAuthor()),
                book.getGenres().stream().map(genreConverter::genreToDto).collect(Collectors.toList())
        );
    }
}
