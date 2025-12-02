package ru.otus.hw.hw06.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.AuthorDto;

@Component
public class AuthorConverter {
    public String authorDtoToString(AuthorDto authorDto) {
        return "Id: %d, FullName: %s".formatted(authorDto.getId(), authorDto.getFullName());
    }
}
