package ru.otus.hw.hw08.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.hw08.models.Book;

@RequiredArgsConstructor
public class BookDeleteMongoListener extends AbstractMongoEventListener<Book> {
    private final MongoOperations mongoOperations;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        if (!"books".equals(event.getCollectionName())) {
            return;
        }

        Object id = event.getSource().get("_id");
        if (id == null) {
            return;
        }

        Query query = Query.query(Criteria.where("book.$id").is(id.toString()));
        mongoOperations.remove(query, "comments");
    }
}
