package ru.otus.hw.hw13.controller.author;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw13.controllers.AuthorController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"));
    }
}
