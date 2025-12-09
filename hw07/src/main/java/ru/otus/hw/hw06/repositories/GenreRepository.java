package ru.otus.hw.hw06.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw06.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
