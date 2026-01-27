package ru.otus.hw.hw09.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw09.controllers.AuthorController;
import ru.otus.hw.hw09.dto.AuthorDto;
import ru.otus.hw.hw09.services.AuthorService;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService authorService;

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        List<AuthorDto> expectedAuthors = List.of(
                new AuthorDto(1L, "Author1"),
                new AuthorDto(2L, "Author2"));
        when(authorService.findAll()).thenReturn(expectedAuthors);
        mvc.perform(get("/authors"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", expectedAuthors));
        mvc.perform(get("/authors/"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", expectedAuthors));
    }
}
