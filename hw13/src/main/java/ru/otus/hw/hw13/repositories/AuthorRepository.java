package ru.otus.hw.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw13.models.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByUserId(long userId);
}
