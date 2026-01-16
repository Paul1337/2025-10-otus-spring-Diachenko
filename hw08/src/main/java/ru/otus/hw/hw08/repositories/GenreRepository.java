package ru.otus.hw.hw08.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.hw08.models.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
