package ru.otus.hw.hw10.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = TestExceptionController.class)
public class GlobalControllerHandlerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldHandleGenericException() throws Exception {
        mvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError());
    }
}
