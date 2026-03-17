package ru.otus.hw.hw13.services.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.hw13.models.Comment;
import ru.otus.hw.hw13.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentSecurityService {
    private final CommentRepository commentRepository;

    @Transactional
    public boolean isCommentAuthor(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        
        if (comment.getOwner() == null) {
            return false;
        }
        
        return comment.getOwner().getId() == userId;
    }
}