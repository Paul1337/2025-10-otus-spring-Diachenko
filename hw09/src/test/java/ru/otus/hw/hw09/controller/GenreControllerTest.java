package ru.otus.hw.hw09.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.hw09.controllers.GenreController;
import ru.otus.hw.hw09.dto.GenreDto;
import ru.otus.hw.hw09.services.GenreService;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        List<GenreDto> expectedGenres = List.of(
                new GenreDto(1L, "Genre1"),
                new GenreDto(2L, "Genre2"));
        when(genreService.findAll()).thenReturn(expectedGenres);
        mvc.perform(get("/genres"))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", expectedGenres));
        mvc.perform(get("/genres/"))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", expectedGenres));

    }
}
