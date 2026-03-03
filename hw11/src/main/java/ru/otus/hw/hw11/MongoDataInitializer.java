package ru.otus.hw.hw11;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.models.Author;
import ru.otus.hw.hw11.models.Book;
import ru.otus.hw.hw11.models.Comment;
import ru.otus.hw.hw11.models.Genre;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoDataInitializer {
    private final ReactiveMongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void initOnAppReady() {
        init().doOnError(e -> System.err.println("Mongo init error: " + e.getMessage()))
                .subscribe();
    }

    private Mono<Void> init() {
        try {
            JsonNode root = readJson();
            return insertAuthors(root)
                    .then(insertGenres(root))
                    .then(insertBooks(root))
                    .then(insertComments(root));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    private JsonNode readJson() throws IOException {
        try (InputStream is =
                     new ClassPathResource("data-mongodb.json").getInputStream()) {
            return objectMapper.readTree(is);
        }
    }

    private Mono<Void> insertAuthors(JsonNode root) {
        return mongoTemplate.count(new Query(), Author.class)
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    }

                    List<Author> authors = objectMapper.convertValue(
                            root.get("authors"),
                            new TypeReference<List<Author>>() {}
                    );

                    return mongoTemplate.insertAll(authors).then();
                });
    }

    private Mono<Void> insertGenres(JsonNode root) {
        return mongoTemplate.count(new Query(), Genre.class)
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    }

                    List<Genre> genres = objectMapper.convertValue(
                            root.get("genres"),
                            new TypeReference<List<Genre>>() {}
                    );

                    return mongoTemplate.insertAll(genres).then();
                });
    }

    private Mono<Void> insertBooks(JsonNode root) {
        return mongoTemplate.count(new Query(), Book.class)
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    }

                    List<Book> books = objectMapper.convertValue(
                            root.get("books"),
                            new TypeReference<List<Book>>() {}
                    );

                    return mongoTemplate.insertAll(books).then();
                });
    }

    private Mono<Void> insertComments(JsonNode root) {
        return mongoTemplate.count(new Query(), Comment.class)
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    }

                    JsonNode commentsNode = root.get("comments");

                    return Flux.fromIterable(commentsNode)
                            .flatMap(node -> {
                                String id = node.get("id").asText();
                                String text = node.get("text").asText();
                                String bookId = node.get("bookId").asText();

                                return mongoTemplate.findById(bookId, Book.class)
                                        .switchIfEmpty(Mono.error(
                                                new RuntimeException("Book not found for DBRef: " + bookId)
                                        ))
                                        .map(book -> new Comment(id, text, bookId));
                            })
                            .collectList()
                            .flatMap(comments -> mongoTemplate.insertAll(comments).then());
                });
    }
}