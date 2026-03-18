package ru.otus.hw.hw13.controller.author;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw13.config.SecurityConfiguration;
import ru.otus.hw.hw13.controllers.AuthorController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfiguration.class)
public class AuthorControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void shouldAllowListAuthorsWhenAuthenticated() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDenyListAuthorsWhenNotAuthenticated() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }

}
