package ru.otus.hw.hw13.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestExceptionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GlobalControllerHandlerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldMapGenericRuntimeExceptionToInternalServerException() throws Exception {
        mvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError());
    }

}
