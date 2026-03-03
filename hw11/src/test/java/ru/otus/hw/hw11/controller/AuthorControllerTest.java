package ru.otus.hw.hw11.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.hw11.controllers.AuthorController;

@WebFluxTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldRenderListPageWithCorrectView() throws Exception {
        webTestClient.get()
                .uri("/authors")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }
}
