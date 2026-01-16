package ru.otus.hw.hw08.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoDataInitializer {
    private final MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    @EventListener(ApplicationStartedEvent.class)
    public void init(ApplicationStartedEvent e) throws Exception {
        JsonNode root = readJson();

        insert("authors", root);
        insert("genres", root);
        insert("books", root);
        insert("comments", root);
    }

    private JsonNode readJson() throws IOException {
        try (InputStream is =
                 new ClassPathResource("data-mongodb.json").getInputStream()) {
            return objectMapper.readTree(is);
        }
    }

    private void insert(String collection, JsonNode root) {
        if (mongoTemplate.collectionExists(collection)) {
            return;
        }

        JsonNode node = root.get(collection);
        if (node == null || !node.isArray()) {
            return;
        }

        List<Document> docs = objectMapper.convertValue(
                node,
                new TypeReference<List<Document>>() {}
            );

        mongoTemplate.insert(docs, collection);
    }
}
