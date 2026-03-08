package ru.otus.hw.hw11.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11.controllers.BookRestController;
import ru.otus.hw.hw11.dto.AuthorDto;
import ru.otus.hw.hw11.dto.BookDto;
import ru.otus.hw.hw11.dto.CreateBookDto;
import ru.otus.hw.hw11.dto.GenreDto;
import ru.otus.hw.hw11.dto.UpdateBookDto;
import ru.otus.hw.hw11.services.BookService;
import ru.otus.hw.hw11.services.CommentService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.otus.hw.hw11.TestDb.getDbAuthors;
import static ru.otus.hw.hw11.TestDb.getDbBooks;
import static ru.otus.hw.hw11.TestDb.getDbGenres;

@WebFluxTest(BookRestController.class)
class BookRestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

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
    void shouldCorrectlyCreateBook() {
        var requestDto = new CreateBookDto("new-book", "a1", Set.of("g1", "g2"));

        var returnedBook = new BookDto(
                "b10",
                "new-book",
                dbAuthors.getFirst(),
                List.of(dbGenres.get(0), dbGenres.get(1))
        );

        when(bookService.insert(requestDto))
                .thenReturn(Mono.just(returnedBook));

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .isEqualTo(returnedBook);

        verify(bookService).insert(requestDto);
    }

    @Test
    void shouldCorrectlyUpdateBook() {
        var bookId = "b10";

        var requestDto = new UpdateBookDto(
                "updated-book",
                "a2",
                Set.of("b3", "b4")
        );

        var expectedResponse = new BookDto(
                bookId,
                "updated-book",
                dbAuthors.get(1),
                List.of(dbGenres.get(2), dbGenres.get(3))
        );

        when(bookService.update(any(UpdateBookDto.class)))
                .thenReturn(Mono.just(expectedResponse));

        webTestClient.put()
                .uri("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(expectedResponse);

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
    void shouldCorrectlyDeleteBook() {
        var bookId = "b2";

        when(bookService.deleteById(bookId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();

        verify(bookService).deleteById(bookId);
    }
}