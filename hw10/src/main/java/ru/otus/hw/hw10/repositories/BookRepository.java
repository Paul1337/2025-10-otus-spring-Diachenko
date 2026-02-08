package ru.otus.hw.hw10.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw10.models.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-with-author", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();

    @EntityGraph(value = "book-with-author-genres", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findById(long id);
}
