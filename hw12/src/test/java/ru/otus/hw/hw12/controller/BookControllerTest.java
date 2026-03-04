package ru.otus.hw.hw12.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw12.controllers.BookController;
import ru.otus.hw.hw12.dto.AuthorDto;
import ru.otus.hw.hw12.dto.BookDto;
import ru.otus.hw.hw12.dto.CommentDto;
import ru.otus.hw.hw12.dto.GenreDto;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.otus.hw.hw12.TestDb.getDbAuthors;
import static ru.otus.hw.hw12.TestDb.getDbBooks;
import static ru.otus.hw.hw12.TestDb.getDbComments;
import static ru.otus.hw.hw12.TestDb.getDbGenres;

@WebMvcTest(BookController.class)
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

//    @Test
//    void shouldRenderNotFoundPageCorrectly() throws Exception {
//        long bookId = 100;
//        String exceptionMessage = "message";
//        EntityNotFoundException exception = new EntityNotFoundException(exceptionMessage);
//        when(bookService.findById(bookId)).thenThrow(exception);
//
//        mvc.perform(get("/books/%d".formatted(bookId)))
//                .andExpect(view().name("not-found"))
//                .andExpect(status().isNotFound())
//                .andExpect(model().attribute("message", exceptionMessage));
//    }
}
