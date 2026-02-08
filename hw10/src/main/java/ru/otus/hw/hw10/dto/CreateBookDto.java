package ru.otus.hw.hw10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    private String title;

    private Long authorId;

    private Set<Long> genreIds;
}
