package ru.otus.hw.hw13.controller.genre;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw13.config.SecurityConfiguration;
import ru.otus.hw.hw13.controllers.GenreController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(SecurityConfiguration.class)
public class GenreControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @WithMockUser
    @ValueSource(strings = { "/genres" })
    void shouldAllowGetViewWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = { "/genres" })
    void shouldDenyGetViewWhenNotAuthenticated(String uri) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }
}
