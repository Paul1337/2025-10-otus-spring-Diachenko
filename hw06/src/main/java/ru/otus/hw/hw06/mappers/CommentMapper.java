package ru.otus.hw.hw06.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw06.dto.CommentDto;
import ru.otus.hw.hw06.dto.GenreDto;
import ru.otus.hw.hw06.models.Comment;
import ru.otus.hw.hw06.models.Genre;

@Component
public class CommentMapper {
    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
