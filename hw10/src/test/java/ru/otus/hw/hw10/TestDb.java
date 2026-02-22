package ru.otus.hw.hw10;

import ru.otus.hw.hw10.dto.AuthorDto;
import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.dto.CommentDto;
import ru.otus.hw.hw10.dto.GenreDto;

import java.util.List;
import java.util.stream.IntStream;

public class TestDb {
    public static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    public static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    public static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<CommentDto> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new CommentDto(id,
                        "Comment_" + id
                ))
                .toList();
    }
}
