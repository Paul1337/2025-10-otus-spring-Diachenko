package ru.otus.hw.hw06.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.CommentDto;
import ru.otus.hw.hw06.models.Comment;

@Component
public class CommentMapper {
    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
