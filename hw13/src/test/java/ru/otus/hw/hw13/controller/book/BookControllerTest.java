package ru.otus.hw.hw13.controller.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw13.controllers.BookController;
import ru.otus.hw.hw13.dto.AuthorDto;
import ru.otus.hw.hw13.dto.BookDto;
import ru.otus.hw.hw13.dto.CommentDto;
import ru.otus.hw.hw13.dto.GenreDto;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.otus.hw.hw13.TestDb.getDbAuthors;
import static ru.otus.hw.hw13.TestDb.getDbBooks;
import static ru.otus.hw.hw13.TestDb.getDbComments;
import static ru.otus.hw.hw13.TestDb.getDbGenres;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    private List<CommentDto> dbComments;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbComments = getDbComments();
    }

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(view().name("books/books"));
    }

    @Test
    void shouldRenderBookPageWithCorrectViewAndModelAttributes() throws Exception {
        long bookId = 1L;

        mvc.perform(get("/books/%d".formatted(bookId)))
                .andExpect(view().name("books/book"));
    }

    @Test
    void shouldRenderNewBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/books/new"))
                .andExpect(view().name("books/edit"));
    }

    @Test
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        long bookId = 1L;

        mvc.perform(get("/books/%d/edit".formatted(bookId)))
                .andExpect(view().name("books/edit"));
    }
}
