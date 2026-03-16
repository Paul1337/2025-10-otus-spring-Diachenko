package ru.otus.hw.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw13.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
