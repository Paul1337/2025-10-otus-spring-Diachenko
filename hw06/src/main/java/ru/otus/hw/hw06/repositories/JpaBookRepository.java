package ru.otus.hw.hw06.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.otus.hw.hw06.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        var authorEntityGraph = entityManager.getEntityGraph("book-with-author");
        Map<String, Object> properties = new HashMap<>();
        properties.put(EntityGraph.EntityGraphType.FETCH.getKey(), authorEntityGraph);
        var book = entityManager.find(Book.class, id, properties);
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var authorEntityGraph = entityManager.getEntityGraph("book-with-author");
        var query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint(EntityGraph.EntityGraphType.FETCH.getKey(), authorEntityGraph);
        return query.getResultList();
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
        var book = entityManager.find(Book.class, id);
        entityManager.remove(book);
    }

    private Book insert(Book book) {
        entityManager.persist(book);
        return book;
    }

    private Book update(Book book) {
        return entityManager.merge(book);
    }
}
