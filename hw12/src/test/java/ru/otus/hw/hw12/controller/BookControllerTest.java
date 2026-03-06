package ru.otus.hw.hw12.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw12.config.SecurityConfiguration;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.otus.hw.hw12.TestDb.getDbAuthors;
import static ru.otus.hw.hw12.TestDb.getDbBooks;
import static ru.otus.hw.hw12.TestDb.getDbComments;
import static ru.otus.hw.hw12.TestDb.getDbGenres;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
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
    @WithMockUser
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(view().name("books/books"));
    }

    @Test
    @WithMockUser
    void shouldRenderBookPageWithCorrectViewAndModelAttributes() throws Exception {
        long bookId = 1L;

        mvc.perform(get("/books/%d".formatted(bookId)))
                .andExpect(view().name("books/book"));
    }

    @Test
    @WithMockUser
    void shouldRenderNewBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/books/new"))
                .andExpect(view().name("books/edit"));
    }

    @Test
    @WithMockUser
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        long bookId = 1L;

        mvc.perform(get("/books/%d/edit".formatted(bookId)))
                .andExpect(view().name("books/edit"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "/books", "/books/1", "/books/new", "/books/1/edit" })
    void shouldRedirectToLoginWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }
}
