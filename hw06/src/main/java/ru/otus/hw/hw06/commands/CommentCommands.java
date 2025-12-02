package ru.otus.hw.hw06.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.hw06.converters.CommentConverter;
import ru.otus.hw.hw06.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentDtoToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbbid")
    public String findCommentsByBookId(long bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::commentDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Create comment for book", key = "cins")
    public String createCommentForBookId(String text, long bookId) {
        var commentDto = commentService.create(text, bookId);
        return commentConverter.commentDtoToString(commentDto);
    }

    @ShellMethod(value = "Update comment by id", key = "cupd")
    public void updateCommentById(long commentId, String text) {
        commentService.update(commentId, text);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteCommentById(long commentId) {
        commentService.deleteById(commentId);
    }
}
