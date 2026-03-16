package ru.otus.hw.hw13.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw13.models.Book;
import ru.otus.hw.hw13.repositories.BookRepository;

@Service
@RequiredArgsConstructor
public class BookSecurityService {
    private final BookRepository bookRepository;

    public boolean isBookAuthor(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        
        if (book.getAuthor().getUser() == null) {
            return false;
        }
        
        return book.getAuthor().getUser().getId() == userId;
    }
}