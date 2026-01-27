package ru.otus.hw.hw09.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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
    void shouldHandleEntityNotFoundException() throws Exception {
        mvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"))
                .andExpect(model().attribute("message", "Entity not found"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        mvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("exception"))
                .andExpect(model().attributeExists("stackTrace"));
    }
}
