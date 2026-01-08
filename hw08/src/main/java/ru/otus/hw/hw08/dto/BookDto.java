package ru.otus.hw.hw08.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String id;

    private String title;

    private AuthorDto authorDto;

    private List<GenreDto> genreDtos;
}
