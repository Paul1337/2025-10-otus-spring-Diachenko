package ru.otus.hw.hw10.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw10.dto.CommentDto;
import ru.otus.hw.hw10.models.Comment;

@Component
public class CommentMapper {
    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
