package ru.otus.hw.hw11.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11.dto.AuthorDto;
import ru.otus.hw.hw11.models.Author;

@Component
public class AuthorMapper {
    public AuthorDto authorToDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
