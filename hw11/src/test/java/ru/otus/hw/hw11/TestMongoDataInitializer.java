package ru.otus.hw.hw11;

import org.junit.jupiter.api.TestInstance;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.models.Author;
import ru.otus.hw.hw11.models.Book;
import ru.otus.hw.hw11.models.Comment;
import ru.otus.hw.hw11.models.Genre;

import java.util.List;
import java.util.Set;

@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMongoDataInitializer {

    private final ReactiveMongoTemplate mongoTemplate;

    public TestMongoDataInitializer(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<Void> init() {
        return cleanDatabase()
                .thenMany(insertAuthors())
                .thenMany(insertGenres())
                .thenMany(insertBooks())
                .thenMany(insertComments())
                .then();
    }

    private Mono<Void> cleanDatabase() {
        return mongoTemplate.dropCollection(Comment.class)
                .then(mongoTemplate.dropCollection(Book.class))
                .then(mongoTemplate.dropCollection(Author.class))
                .then(mongoTemplate.dropCollection(Genre.class));
    }

    private Flux<Author> insertAuthors() {
        List<Author> authors = List.of(
                new Author("a1", "Author_1"),
                new Author("a2", "Author_2"),
                new Author("a3", "Author_3")
        );
        return mongoTemplate.insertAll(authors);
    }

    private Flux<Genre> insertGenres() {
        List<Genre> genres = List.of(
                new Genre("g1", "Genre_1"),
                new Genre("g2", "Genre_2"),
                new Genre("g3", "Genre_3"),
                new Genre("g4", "Genre_4"),
                new Genre("g5", "Genre_5"),
                new Genre("g6", "Genre_6")
        );
        return mongoTemplate.insertAll(genres);
    }

    private Flux<Book> insertBooks() {
        Author a1 = new Author("a1", "Author_1");
        Author a2 = new Author("a2", "Author_2");
        Author a3 = new Author("a3", "Author_3");

        Genre g1 = new Genre("g1", "Genre_1");
        Genre g2 = new Genre("g2", "Genre_2");
        Genre g3 = new Genre("g3", "Genre_3");
        Genre g4 = new Genre("g4", "Genre_4");
        Genre g5 = new Genre("g5", "Genre_5");
        Genre g6 = new Genre("g6", "Genre_6");

        List<Book> books = List.of(
                new Book("b1", "BookTitle_1", a1, Set.of(g1, g2)),
                new Book("b2", "BookTitle_2", a2, Set.of(g3, g4)),
                new Book("b3", "BookTitle_3", a3, Set.of(g5, g6))
        );

        return mongoTemplate.insertAll(books);
    }

    private Flux<Comment> insertComments() {
        List<Comment> comments = List.of(
                new Comment("c1", "Comment_1", "b1"),
                new Comment("c2", "Comment_2", "b1"),
                new Comment("c3", "Comment_3", "b2"),
                new Comment("c4", "Comment_4", "b2"),
                new Comment("c5", "Comment_5", "b2")
        );

        return mongoTemplate.insertAll(comments);
    }
}