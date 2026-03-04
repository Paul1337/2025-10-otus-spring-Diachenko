package ru.otus.hw.hw12.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw12.dto.CommentDto;
import ru.otus.hw.hw12.models.Comment;

@Component
public class CommentMapper {
    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
