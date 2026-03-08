package ru.otus.hw.hw11.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.hw11.controllers.BookController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebFluxTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldRenderListPageWithCorrectView() {
        webTestClient.get()
                .uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void shouldRenderBookPageWithCorrectView() {
        long bookId = 1L;

        webTestClient.get()
                .uri("/books/{id}", bookId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void shouldRenderNewBookPageWithCorrectView() {
        webTestClient.get()
                .uri("/books/new")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void shouldRenderEditBookPageWithCorrectView() {
        long bookId = 1L;

        webTestClient.get()
                .uri("/books/{id}/edit", bookId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }
}
