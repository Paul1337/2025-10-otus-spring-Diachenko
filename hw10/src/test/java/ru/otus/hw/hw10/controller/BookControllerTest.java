package ru.otus.hw.hw10.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw10.controllers.BookController;
import ru.otus.hw.hw10.dto.AuthorDto;
import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.dto.CommentDto;
import ru.otus.hw.hw10.dto.GenreDto;
import ru.otus.hw.hw10.exceptions.EntityNotFoundException;
import ru.otus.hw.hw10.services.AuthorService;
import ru.otus.hw.hw10.services.BookService;
import ru.otus.hw.hw10.services.CommentService;
import ru.otus.hw.hw10.services.GenreService;
import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.otus.hw.hw10.TestDb.getDbAuthors;
import static ru.otus.hw.hw10.TestDb.getDbBooks;
import static ru.otus.hw.hw10.TestDb.getDbComments;
import static ru.otus.hw.hw10.TestDb.getDbGenres;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

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
        when(bookService.findAll()).thenReturn(dbBooks);
        mvc.perform(get("/books"))
                .andExpect(view().name("books/books"))
                .andExpect(model().attribute("books", dbBooks));
        mvc.perform(get("/books/"))
                .andExpect(view().name("books/books"))
                .andExpect(model().attribute("books", dbBooks));

    }

    @Test
    void shouldRenderBookPageWithCorrectViewAndModelAttributes() throws Exception {
        long bookId = 1L;
        BookDto expectedBook = dbBooks.stream().filter(bookDto -> bookDto.getId() == bookId).findFirst().orElseThrow();
        List<CommentDto> expectedComments = List.of(dbComments.get(0), dbComments.get(1));

        when(bookService.findById(1L)).thenReturn(expectedBook);
        when(commentService.findAllByBookId(1L)).thenReturn(expectedComments);

        mvc.perform(get("/books/%d".formatted(bookId)))
                .andExpect(view().name("books/book"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments));
    }

    @Test
    void shouldRenderNewBookPageWithCorrectViewAndModelAttributes() throws Exception {
        when(authorService.findAll()).thenReturn(dbAuthors);
        when(genreService.findAll()).thenReturn(dbGenres);

        mvc.perform(get("/books/new"))
                .andExpect(view().name("books/edit"))
                .andExpect(model().attribute("authors", dbAuthors))
                .andExpect(model().attribute("genres", dbGenres))
                .andExpect(model().attribute("book", hasProperty("id", is(0L))));
    }

    @Test
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        long bookId = 1L;

        when(authorService.findAll()).thenReturn(dbAuthors);
        when(genreService.findAll()).thenReturn(dbGenres);
        when(bookService.findById(bookId)).thenReturn(dbBooks.getFirst());

        mvc.perform(get("/books/%d/edit".formatted(bookId)))
                .andExpect(view().name("books/edit"))
                .andExpect(model().attribute("authors", dbAuthors))
                .andExpect(model().attribute("genres", dbGenres))
                .andExpect(model().attribute("book", dbBooks.getFirst()));
    }

    @Test
    void shouldRenderNotFoundPageCorrectly() throws Exception {
        long bookId = 100;
        String exceptionMessage = "message";
        EntityNotFoundException exception = new EntityNotFoundException(exceptionMessage);
        when(bookService.findById(bookId)).thenThrow(exception);

        mvc.perform(get("/books/%d".formatted(bookId)))
                .andExpect(view().name("not-found"))
                .andExpect(status().isNotFound())
                .andExpect(model().attribute("message", exceptionMessage));
    }
}
