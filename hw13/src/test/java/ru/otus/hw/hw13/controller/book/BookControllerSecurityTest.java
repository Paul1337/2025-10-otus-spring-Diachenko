package ru.otus.hw.hw13.controller.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw13.config.SecurityConfiguration;
import ru.otus.hw.hw13.controllers.BookController;
import ru.otus.hw.hw13.controllers.BookRestController;
import ru.otus.hw.hw13.dto.AuthorDto;
import ru.otus.hw.hw13.dto.BookDto;
import ru.otus.hw.hw13.dto.CreateBookDto;
import ru.otus.hw.hw13.dto.GenreDto;
import ru.otus.hw.hw13.dto.UpdateBookDto;
import ru.otus.hw.hw13.services.book.BookService;
import ru.otus.hw.hw13.services.comment.CommentService;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ BookController.class, BookRestController.class })
@Import(SecurityConfiguration.class)
public class BookControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    @ParameterizedTest
    @WithMockUser
    @ValueSource(strings = { "/books", "/books/1", "/books/new", "/books/1/edit" })
    void shouldAllowGetViewWhenAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isOk());
    }


    @ParameterizedTest
    @ValueSource(strings = { "/books", "/books/1", "/books/new", "/books/1/edit" })
    void shouldDenyGetViewWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }

    @ParameterizedTest
    @WithMockUser
    @ValueSource(strings = { "/api/books" })
    void shouldAllowGetApiWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isOk());
    }


    @ParameterizedTest
    @ValueSource(strings = { "/api/books" })
    void shouldDenyGetApiWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldAllowCreateBookWhenAuthenticated() throws Exception {
        var createBookRequest = new CreateBookDto("new-book", 1L, Set.of(1L, 2L));
        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDenyCreateBookWhenNotAuthenticated() throws Exception {
        mvc.perform(post("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldAllowUpdateBookWhenAuthenticated() throws Exception {
        var createBookRequest = new UpdateBookDto("updated-book", 2L, Set.of(1L, 3L));
        mvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDenyUpdateBookWhenNotAuthenticated() throws Exception {
        mvc.perform(put("/api/books/1"))
                .andExpect(status().isUnauthorized());
    }


    @ParameterizedTest
    @WithMockUser
    @ValueSource(strings = { "/api/books/1" })
    void shouldAllowDeleteApiWhenAuthenticated(String uri) throws Exception {
        mvc.perform(delete(uri))
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @ValueSource(strings = { "/api/books/1" })
    void shouldDenyDeleteApiWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(delete(uri))
                .andExpect(status().isUnauthorized());
    }

}
