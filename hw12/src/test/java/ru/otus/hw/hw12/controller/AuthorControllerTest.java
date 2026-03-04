package ru.otus.hw.hw12.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw12.controllers.AuthorController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(view().name("authors"));
    }
}
