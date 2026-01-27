package ru.otus.hw.hw09.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw09.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
