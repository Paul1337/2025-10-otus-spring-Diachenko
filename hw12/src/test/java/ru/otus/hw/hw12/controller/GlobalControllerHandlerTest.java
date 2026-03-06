package ru.otus.hw.hw12.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw12.config.SecurityConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestExceptionController.class)
@Import(SecurityConfiguration.class)
public class GlobalControllerHandlerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void shouldHandleGenericException() throws Exception {
        mvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError());
    }

}
