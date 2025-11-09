package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbcNamedParams;

    private final JdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var book = jdbcNamedParams.query("select book.id, book.title, book.author_id, " +
                "author.full_name as author_full_name, genre.id as genre_id, genre.name as genre_name from books book " +
                "left join books_genres bg on bg.book_id = book.id left join genres genre on genre.id = bg.genre_id " +
                "left join authors author on author.id = book.author_id  where book.id = :id",
                Map.of("id", id), new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var books = getAllBooksWithoutGenres();
        var relations = getAllGenreRelations();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcNamedParams.update("delete from books where id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("select book.id, book.title, book.author_id, author.full_name as author_full_name " +
                "from books book left join authors author on author.id = author_id", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres", new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> genreById = genres.stream().collect(Collectors.toMap(Genre::getId, genre -> genre));

        Map<Long, List<Genre>> genresByBookId = relations.stream().collect(Collectors.toMap(
                BookGenreRelation::bookId,
                (relation) -> new ArrayList<>(List.of(genreById.get(relation.genreId()))),
                (existingList, newList) -> {
                    existingList.addAll(newList);
                    return existingList;
                }
        ));

        booksWithoutGenres.forEach(book -> {
            var genresForBook = genresByBookId.get(book.getId());
            book.setGenres(genresForBook);
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        jdbcNamedParams.update("insert into books (title, author_id) values (:title, :author_id)",
                params, keyHolder, new String[] { "id" });

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var params = Map.of("title", book.getTitle(),
                            "author_id", book.getAuthor().getId(),
                            "id", book.getId());
        int rowsAffected = jdbcNamedParams.update(
                "update books set title = :title, author_id = :author_id where id = :id", params
        );

        if (rowsAffected == 0) {
            throw new EntityNotFoundException("Book with id %s does not exist".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        MapSqlParameterSource[] params = book.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("book_id", book.getId())
                        .addValue("genre_id", genre.getId()))
                .toArray(MapSqlParameterSource[]::new);

        jdbcNamedParams.batchUpdate("insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)", params);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcNamedParams.update("delete from books_genres where book_id = :book_id", Map.of("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("id"), rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("author_full_name")), List.of());
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {
        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }

            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(new Author(rs.getLong("author_id"), rs.getString("author_full_name")));

            List<Genre> genres = new ArrayList<>();

            do {
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            } while (rs.next());
            book.setGenres(genres);

            return book;
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {
        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
