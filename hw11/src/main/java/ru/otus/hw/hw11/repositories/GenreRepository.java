package ru.otus.hw.hw11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw11.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
