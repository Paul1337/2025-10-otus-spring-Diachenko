package ru.otus.hw.hw06.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.CommentDto;

@Component
public class CommentConverter {
    public String commentDtoToString(CommentDto commentDto) {
        return "Id: %d, Text: %s".formatted(commentDto.getId(), commentDto.getText());
    }
}
