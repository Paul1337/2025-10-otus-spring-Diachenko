package ru.otus.hw.hw06.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw06.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
