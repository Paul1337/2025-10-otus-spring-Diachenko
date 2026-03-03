package ru.otus.hw.hw11.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.hw11.controllers.GenreController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebFluxTest(GenreController.class)
public class GenreControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        webTestClient.get()
                .uri("/genres")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(result ->
                        assertThat(result.getResponseBody())
                                .contains("genres")
                );

    }
}
