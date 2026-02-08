package ru.otus.hw.hw10.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw10.dto.AuthorDto;
import ru.otus.hw.hw10.models.Author;

@Component
public class AuthorMapper {
    public AuthorDto authorToDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
