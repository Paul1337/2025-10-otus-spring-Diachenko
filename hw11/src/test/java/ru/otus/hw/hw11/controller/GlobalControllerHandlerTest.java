package ru.otus.hw.hw11.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = TestExceptionController.class)
class GlobalControllerHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldHandleGenericException() {
        webTestClient.get()
                .uri("/test/error")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}