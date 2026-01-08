package ru.otus.hw.hw08.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw08.dto.CommentDto;

@Component
public class CommentConverter {
    public String commentDtoToString(CommentDto commentDto) {
        return "Id: %s, Text: %s".formatted(commentDto.getId(), commentDto.getText());
    }
}
