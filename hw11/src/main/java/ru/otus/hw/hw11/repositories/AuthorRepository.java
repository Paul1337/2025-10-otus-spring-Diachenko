package ru.otus.hw.hw11.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.hw11.models.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, String> {
}
