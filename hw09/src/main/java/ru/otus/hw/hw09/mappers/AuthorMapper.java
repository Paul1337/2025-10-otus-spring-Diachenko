package ru.otus.hw.hw09.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw09.dto.AuthorDto;
import ru.otus.hw.hw09.models.Author;

@Component
public class AuthorMapper {
    public AuthorDto authorToDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
