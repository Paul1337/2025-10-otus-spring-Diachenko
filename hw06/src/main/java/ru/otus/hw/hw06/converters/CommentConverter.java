package ru.otus.hw.hw06.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.CommentDto;
import ru.otus.hw.hw06.models.Comment;

@Component
public class CommentConverter {
    public String commentDtoToString(CommentDto commentDto) {
        return "Id: %d, Text: %s".formatted(commentDto.getId(), commentDto.getText());
    }
}
