package ru.otus.hw.hw11.models.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw11.models.Book;

@RequiredArgsConstructor
@Component
public class BookDeleteMongoListener extends AbstractMongoEventListener<Book> {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        if (!"books".equals(event.getCollectionName())) {
            return;
        }

        Object id = event.getSource().get("_id");
        if (id == null) {
            return;
        }

        mongoTemplate.remove(
                Query.query(Criteria.where("book.$id").is(id.toString())),
                "comments"
        );
    }
}
