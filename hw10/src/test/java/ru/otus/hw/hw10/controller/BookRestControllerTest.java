package ru.otus.hw.hw10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw10.controllers.BookRestController;
import ru.otus.hw.hw10.dto.AuthorDto;
import ru.otus.hw.hw10.dto.BookDto;
import ru.otus.hw.hw10.dto.CreateBookDto;
import ru.otus.hw.hw10.dto.GenreDto;
import ru.otus.hw.hw10.dto.UpdateBookDto;
import ru.otus.hw.hw10.services.BookService;
import ru.otus.hw.hw10.services.CommentService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.hw10.TestDb.getDbAuthors;
import static ru.otus.hw.hw10.TestDb.getDbBooks;
import static ru.otus.hw.hw10.TestDb.getDbGenres;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {
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

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @Test
    void shouldCorrectlyCreateBook() throws Exception {
        var requestDto = new CreateBookDto("new-book", 1L, Set.of(1L, 2L));

        var returnedBook = new BookDto(
                10L,
                "new-book",
                dbAuthors.getFirst(),
                List.of(dbGenres.get(0), dbGenres.get(1))
        );

        when(bookService.insert(requestDto)).thenReturn(returnedBook);

        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(returnedBook)
                ));

        verify(bookService).insert(requestDto);
    }

    @Test
    void shouldCorrectlyUpdateBook() throws Exception {
        var bookId = 10L;

        var requestDto = new UpdateBookDto(
                "updated-book",
                2L,
                Set.of(3L, 4L)
        );

        var expectedResponse = new BookDto(
                bookId,
                "updated-book",
                dbAuthors.get(1),
                List.of(dbGenres.get(2), dbGenres.get(3))
        );

        when(bookService.update(any(UpdateBookDto.class)))
                .thenReturn(expectedResponse);

        mvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(expectedResponse)
                ));

        ArgumentCaptor<UpdateBookDto> captor =
                ArgumentCaptor.forClass(UpdateBookDto.class);

        verify(bookService).update(captor.capture());

        UpdateBookDto actualUpdateDto = captor.getValue();

        requestDto.setId(bookId);
        assertThat(actualUpdateDto)
                .usingRecursiveComparison()
                .isEqualTo(requestDto);
    }

    @Test
    void shouldCorrectlyDeleteBook() throws Exception {
        long bookId = 2L;

        mvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(bookService).deleteById(bookId);
    }



}
